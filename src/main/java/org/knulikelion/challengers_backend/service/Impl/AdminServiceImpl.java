package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.*;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.AdminNotice;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.ExtraUserMapping;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.AdminNoticeRepository;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.ExtraUserMappingRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.AdminService;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final ExtraUserMappingRepository extraUserMappingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClubRepository clubRepository;
    private final AdminNoticeRepository adminNoticeRepository;

    public AdminServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider,
                            ClubRepository clubRepository,
                            ExtraUserMappingRepository extraUserMappingRepository,
                            AdminNoticeRepository adminNoticeRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clubRepository = clubRepository;
        this.extraUserMappingRepository = extraUserMappingRepository;
        this.adminNoticeRepository = adminNoticeRepository;
    }

    @Override
    public SignResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {
        User user = userRepository.getByEmail(signInRequestDto.getEmail());

        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            return SignResponseDto.builder()
                    .success(false)
                    .msg("비밀번호가 일치하지 않습니다.")
                    .build();
        }

        if (!user.getRoles().contains("ROLE_ADMIN")) {
            return SignResponseDto.builder()
                    .success(false)
                    .msg("권한이 없는 사용자입니다.")
                    .build();
        }

        return SignResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(String.valueOf(user.getEmail()), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(String.valueOf(user.getEmail())))
                .email(user.getEmail())
                .uid(user.getUserName())
                .success(true)
                .msg("성공적으로 로그인되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto assignAdministrator(AssignAdministratorRequestDto assignAdministratorRequestDto) {
        User user = userRepository.getByEmail(assignAdministratorRequestDto.getEmail());
        BaseResponseDto baseResponseDto = new BaseResponseDto();

//        사용자를 찾을 수 없을 때
        if(user == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 사용자를 찾을 수 없습니다.")
                    .build();
        }

//        이미 사용자가 관리자 권한을 가지고 있을 때
        if(user.getRoles().contains("ROLE_ADMIN")) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("이미 관리자 권한을 가지고 있는 사용자입니다.")
                    .build();
        }

//        사용자에게 관리자 권한 추가
        user.getRoles().add("ROLE_ADMIN");

        ExtraUserMapping extraUserMapping = new ExtraUserMapping();
        extraUserMapping.setUser(user);
        extraUserMapping.setRole(assignAdministratorRequestDto.getRole());
        extraUserMapping.setProfileUrl(assignAdministratorRequestDto.getProfileUrl());

        extraUserMappingRepository.save(extraUserMapping);

        return BaseResponseDto.builder()
                .success(true)
                .msg("관리자 권한 추가가 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto postNoti(NoticeRequestDto noticeRequestDto, String email) {
        User user = userRepository.getByEmail(email);

        if(user == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 사용자를 찾을 수 없습니다.")
                    .build();
        }

        if(noticeRequestDto.getTitle() == null && noticeRequestDto.getContent() == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("제목 또는 공지사항에 빈 내용이 입력되었습니다.")
                    .build();
        }

        AdminNotice adminNotice = AdminNotice.builder()
                .user(user)
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .build();

        adminNoticeRepository.save(adminNotice);

        return BaseResponseDto.builder()
                .success(true)
                .msg("공지사항 등록이 완료되었습니다.")
                .build();
    }

    @Override
    public List<NoticeResponseDto> getAllNoti() {
        List<AdminNotice> adminNoticeList = adminNoticeRepository.findAll();
        List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();

        for(AdminNotice adminNotice : adminNoticeList) {
            ExtraUserMapping extraUserMapping = extraUserMappingRepository.getByUserId(adminNotice.getUser().getId());

            noticeResponseDtoList.add(NoticeResponseDto.builder()
                            .title(adminNotice.getTitle())
                            .content(adminNotice.getContent())
                            .uploadedUserName(extraUserMapping.getUser().getUserName())
                            .uploadedUserRole(extraUserMapping.getRole())
                            .uploadedUserProfileUrl(extraUserMapping.getProfileUrl())
                            .updatedAt(adminNotice.getUpdatedAt().toString())
                            .createdAt(adminNotice.getCreatedAt().toString())
                            .id(adminNotice.getId())
                    .build());
        }

        return noticeResponseDtoList;
    }

    @Override
    public NoticeResponseDto getNotiDetail(Long id) {
        AdminNotice notice = adminNoticeRepository.getById(id);
        ExtraUserMapping extraUserMapping = extraUserMappingRepository.getByUserId(notice.getUser().getId());

        return NoticeResponseDto.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .uploadedUserName(notice.getUser().getUserName())
                .uploadedUserProfileUrl(extraUserMapping.getProfileUrl())
                .uploadedUserRole(extraUserMapping.getRole())
                .createdAt(notice.getCreatedAt().toString())
                .updatedAt(notice.getUpdatedAt().toString())
                .build();
    }

    @Override
    public BaseResponseDto deleteNoti(Long id) {
        AdminNotice notice = adminNoticeRepository.getById(id);
        adminNoticeRepository.delete(notice);

        return BaseResponseDto.builder()
                .success(true)
                .msg("공지사항 삭제가 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto updateNoti(UpdateNoticeRequestDto updateNoticeRequestDto, String email) {
        AdminNotice notice = adminNoticeRepository.getById(updateNoticeRequestDto.getId());

        if(notice == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 공지사항을 찾을 수 없습니다.")
                    .build();
        }

        if(updateNoticeRequestDto.getContent() == null && updateNoticeRequestDto.getTitle() == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("수정 사항을 입력하지 않았습니다.")
                    .build();
        }

        if(userRepository.getByEmail(email).getId() != notice.getUser().getId()) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 공지사항 작성자가 아닙니다.")
                    .build();
        }

        notice.setTitle(updateNoticeRequestDto.getTitle());
        notice.setContent(updateNoticeRequestDto.getContent());
        notice.setUpdatedAt(LocalDateTime.now());

        adminNoticeRepository.save(notice);

        return BaseResponseDto.builder()
                .success(true)
                .msg("공지사항 업데이트가 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto changeName(String email, String name) {
        User user = userRepository.getByEmail(email);

        if(user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없음");
        }

        if(user.getUserName().equals(name)) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("현재 이름과 일치합니다.")
                    .build();
        }

        user.setUserName(name);

        return BaseResponseDto.builder()
                .success(true)
                .msg("이름 변경이 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto changeProfile(String email, String url) {
        User user = userRepository.getByEmail(email);
        ExtraUserMapping extraUserMapping = extraUserMappingRepository.getByUserId(user.getId());

        if(user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없음");
        }

        extraUserMapping.setProfileUrl(url);
        return BaseResponseDto.builder()
                .success(true)
                .msg("프로필 사진 변경이 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto changePw(ChangePasswordRequestDto changePasswordRequestDto, String email) {
        User user = userRepository.getByEmail(email);

        if(user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없음");
        }

        if(!passwordEncoder.matches(changePasswordRequestDto.getUserPw(), user.getPassword())) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("사용자의 비밀번호가 일치하지 않습니다.")
                    .build();
        }

        if(!passwordEncoder.matches(changePasswordRequestDto.getChangePw(), user.getPassword())) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("현재 비밀번호와 일치합니다.")
                    .build();
        }

        user.setPassword(passwordEncoder.encode(changePasswordRequestDto.getChangePw()));
        userRepository.save(user);
        return BaseResponseDto.builder()
                .success(true)
                .msg("비밀번호 변경이 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto changeRole(String role, String email) {
        User user = userRepository.getByEmail(email);
        ExtraUserMapping extraUserMapping = extraUserMappingRepository.getByUserId(user.getId());

        if(user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없음");
        }

        if(role.equals(extraUserMapping.getRole())) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("역할이 변경되지 않았습니다.")
                    .build();
        }

        extraUserMapping.setRole(role);
        extraUserMappingRepository.save(extraUserMapping);

        return BaseResponseDto.builder()
                .success(true)
                .msg("역할 변경이 완료되었습니다.")
                .build();
    }

    @Override
    public Page<AdminClubResponseDto> getAllClubs(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC,"createdAt"));
        Page<Club> clubPage = clubRepository.findAll(pageRequest);

        List<AdminClubResponseDto> adminClubResponseDtoList = new ArrayList<>();

        for (Club temp : clubPage.getContent()) {
            if(temp.getClubManager() == null) {
                adminClubResponseDtoList.add(
                        AdminClubResponseDto.builder()
                                .id(temp.getId())
                                .clubName(temp.getClubName())
                                .masterName(null)
                                .masterEmail(null)
                                .build());
            } else {
                adminClubResponseDtoList.add(
                        AdminClubResponseDto.builder()
                                .id(temp.getId())
                                .clubName(temp.getClubName())
                                .masterName(temp.getClubManager().getUserName())
                                .masterEmail(temp.getClubManager().getEmail())
                                .build());
            }
        }

        return new PageImpl<>(adminClubResponseDtoList, pageRequest, clubPage.getTotalElements());
    }
}
