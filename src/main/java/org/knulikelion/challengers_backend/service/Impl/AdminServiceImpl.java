package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.*;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.enums.EventType;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;
import org.knulikelion.challengers_backend.data.repository.*;
import org.knulikelion.challengers_backend.service.AdminService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final ExtraUserMappingRepository extraUserMappingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final AdminNoticeRepository adminNoticeRepository;
    private final ProjectRepository projectRepository;
    private final ProjectAuditRepository projectAuditRepository;
    private final ClubAuditRepository clubAuditRepository;
    private final UserAuditRepository userAuditRepository;
    private final AdminHomeFeedRepository adminHomeFeedRepository;
    private final ClubService clubService;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtTokenProvider jwtTokenProvider,
                            ClubRepository clubRepository,
                            ExtraUserMappingRepository extraUserMappingRepository,
                            UserClubRepository userClubRepository,
                            AdminNoticeRepository adminNoticeRepository,
                            ProjectRepository projectRepository, ProjectAuditRepository projectAuditRepository, ClubAuditRepository clubAuditRepository, UserAuditRepository userAuditRepository, AdminHomeFeedRepository adminHomeFeedRepository, ClubService clubService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.clubRepository = clubRepository;
        this.extraUserMappingRepository = extraUserMappingRepository;
        this.userClubRepository = userClubRepository;
        this.adminNoticeRepository = adminNoticeRepository;
        this.projectRepository = projectRepository;
        this.projectAuditRepository = projectAuditRepository;
        this.clubAuditRepository = clubAuditRepository;
        this.userAuditRepository = userAuditRepository;
        this.adminHomeFeedRepository = adminHomeFeedRepository;
        this.clubService = clubService;
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
    public BaseResponseDto removeClubMember(Long clubId, List<Long> userId) {
        if(clubRepository.findById(clubId).isPresent()) {
            for(Long temp : userId) {
                UserClub userClub = userClubRepository.findByUserIdAndClubId(temp, clubId);
                if(userClub != null) {
                    userClubRepository.delete(userClub);
                }
            }

            return BaseResponseDto.builder()
                    .success(true)
                    .msg("작업이 완료되었습니다.")
                    .build();
        } else {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 클럽을 찾을 수 없습니다.")
                    .build();
        }
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

    @Override
    public BaseResponseDto changeClubStatus(Long clubId, String status) {
        Club club = clubRepository.getById(clubId);
        String clubStatus = status.toUpperCase();

        if(club == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("작업을 완료할 수 없습니다.")
                    .build();
        }

        // 관리자가 승인 해야 클럽 생성자가 클럽 멤버로 들어감.
        if(clubStatus.equals("ACCEPT")) {
            club.setClubApproved(true);
            clubRepository.save(club);
            clubService.addMember(club.getClubManager().getId(),clubId);
        } else {
            club.setClubApproved(false);
            clubRepository.save(club);
        }

        return BaseResponseDto.builder()
                .success(true)
                .msg("클럽 상태 변경이 완료되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto changeProjectStatus(Long projectId, ProjectStatus status) {
        Project project = projectRepository.getById(projectId);
        if(project == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("발견된 프로젝트가 없습니다.")
                    .build();
        }

        if(status == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("상태를 업데이트 할 수 없습니다.")
                    .build();
        } else {
            project.setStatus(status);
            return BaseResponseDto.builder()
                    .success(true)
                    .msg("상태가 업데이트 되었습니다.")
                    .build();
        }
    }

    @Override
    public Page<AllProjectResponseDto> getAllProject(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Project> projects = projectRepository.findAll(pageable);

        return projects.map(project -> {
            AllProjectResponseDto allProjectResponseDto = new AllProjectResponseDto();

            allProjectResponseDto.setId(project.getId());
            allProjectResponseDto.setProjectName(project.getProjectName());
            allProjectResponseDto.setProjectCategory(project.getProjectCategory());
            allProjectResponseDto.setProjectDescription(project.getProjectDescription());
            allProjectResponseDto.setImageUrl(project.getImageUrl());
            if(project.getClub() == null) {
                allProjectResponseDto.setBelongedClubName("소속된 클럽이 없음");
            } else {
                allProjectResponseDto.setBelongedClubName(project.getClub().getClubName());
            }

            return allProjectResponseDto;
        });
    }

//       대시 보드
    @Override
    public Long countUsers() {
        return userRepository.count();
    }

    @Override
    public Long countClubs() {
        return clubRepository.count();
    }

    @Override
    public Long countProjects() {
        return projectRepository.count();
    }

    @Override
    public Long countTodayAddProjects() {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(1);
        return projectRepository.countByCreatedAtBetween(start,end);
    }

    @Override
    public Long countTodayAddClubs() {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(1);
        return clubRepository.countByCreatedAtBetween(start,end);
    }

    @Override
    public Long countTodayAddUsers() {
        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(1);
        return userRepository.countByCreatedAtBetween(start,end);
    }

    @Override
    public Long countTodayDeletedProjects() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return projectAuditRepository.countByDeletedAtBetween(start, end);
    }

    @Override
    public Long countTodayDeletedClubs() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return clubAuditRepository.countByDeletedAtBetween(start,end);
    }

    @Override
    public Long countTodayDeletedUsers() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(),LocalTime.MAX);

        return userAuditRepository.countByDeletedAtBetween(start,end);
    }

    @Override
    public Long countDeletedUsers() {
        return userAuditRepository.count();
    }

    @Override
    public BaseResponseDto createHomeFeed(AdminHomeFeedRequestDto adminHomeFeedDto,String token) {
        AdminHomeFeed adminHomeFeed = new AdminHomeFeed();
        User founduser = userRepository.getByEmail(jwtTokenProvider.getUserEmail(token));
        if (founduser == null) {
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("존재하지 않는 사용자");

            return baseResponseDto;
        }else {
            adminHomeFeed.setName(founduser.getUserName());
            adminHomeFeed.setRole(founduser.getRoles().toString());
            adminHomeFeed.setImage(adminHomeFeedDto.getImage());
            adminHomeFeed.setContents(adminHomeFeedDto.getContents());
            adminHomeFeed.setCreatedAt(LocalDateTime.now());
            adminHomeFeedRepository.save(adminHomeFeed);

            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(true)
                    .msg("홈피드 생성 완료")
                    .build();
            return baseResponseDto;
        }
    }

    @Override
    public Object getHomeFeed(Long id) {
        Optional<AdminHomeFeed> feed = adminHomeFeedRepository.findById(id);
        if (feed.isEmpty()) {
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("홈피드가 존재하지 않음");
            return baseResponseDto;
        }else {
            AdminHomeFeed selectedFeed = feed.get();
            AdminHomeFeedResponseDto feedDto = new AdminHomeFeedResponseDto();

            feedDto.setName(selectedFeed.getName());
            feedDto.setRole(selectedFeed.getRole());
            feedDto.setContents(selectedFeed.getContents());
            feedDto.setImage(selectedFeed.getImage());

            return feedDto;
        }
    }

    @Override
    public List<AdminHomeFeedResponseDto> getAllHomeFeed() {
        List<AdminHomeFeed> feeds = adminHomeFeedRepository.findAllByOrderByCreatedAtDesc();

        return feeds.stream().map(feed -> {
            AdminHomeFeedResponseDto dto = new AdminHomeFeedResponseDto();
            dto.setName(feed.getName());
            dto.setRole(feed.getRole());
            dto.setContents(feed.getContents());
            dto.setImage(feed.getImage());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public BaseResponseDto updateHomeFeed(AdminHomeFeedRequestDto adminHomeFeedRequestDto,Long feedId) {
        Optional<AdminHomeFeed> feed = adminHomeFeedRepository.findById(feedId);

        if (feed.isEmpty()) {
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("홈피드가 존재하지 않음");
            return baseResponseDto;
        }else {
            AdminHomeFeed selectedFeed = feed.get();

            selectedFeed.setContents(adminHomeFeedRequestDto.getContents());
            selectedFeed.setImage(adminHomeFeedRequestDto.getImage());
            adminHomeFeedRepository.save(selectedFeed);

            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(true)
                    .msg("홈피드 수정 완료")
                    .build();
            return baseResponseDto;
        }
    }

    @Override
    public BaseResponseDto deleteHomeFeed(Long feedId) {
        Optional<AdminHomeFeed> feed = adminHomeFeedRepository.findById(feedId);

        if(feed.isEmpty()) {
            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(false)
                    .msg("홈피드가 존재하지 않음")
                    .build();
            return baseResponseDto;
        }else {
            adminHomeFeedRepository.deleteById(feedId);

            BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                    .success(true)
                    .msg("홈 피드 삭제 완료")
                    .build();
            return baseResponseDto;
        }

    }


    @Override
    public List<ProjectAuditResponseDto> getLatestCreatedProject() {
        List<ProjectAudit> audits = projectAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.CREATED);

        return audits.stream().map(audit -> {
            ProjectAuditResponseDto dto = new ProjectAuditResponseDto();
            dto.setProjectId(audit.getProjectId());
            dto.setProjectName(audit.getProjectName());
            dto.setCreatedBy(audit.getCreatedBy());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectAuditResponseDto> getLatestDeletedProject() {
        List<ProjectAudit> audits = projectAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.DELETED);

        return audits.stream().map(audit -> {
            ProjectAuditResponseDto dto = new ProjectAuditResponseDto();
            dto.setProjectId(audit.getProjectId());
            dto.setProjectName(audit.getProjectName());
            dto.setCreatedBy(audit.getCreatedBy());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }
    @Override
    public List<ClubAuditResponseDto> getLatestCreatedClub() {
        List<ClubAudit> audits = clubAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.CREATED);

        return audits.stream().map(audit -> {
            ClubAuditResponseDto dto = new ClubAuditResponseDto();
            dto.setClubId(audit.getClubId());
            dto.setClubName(audit.getClubName());
            dto.setCreatedBy(audit.getCreatedBy());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ClubAuditResponseDto> getLatestDeletedClub() {
        List<ClubAudit> audits = clubAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.DELETED);

        return audits.stream().map(audit -> {
            ClubAuditResponseDto dto = new ClubAuditResponseDto();
            dto.setClubId(audit.getClubId());
            dto.setClubName(audit.getClubName());
            dto.setCreatedBy(audit.getCreatedBy());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserAuditResponseDto> getLatestCreatedUser() {
        List<UserAudit> audits = userAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.CREATED);

        return audits.stream().map(audit -> {
            UserAuditResponseDto dto = new UserAuditResponseDto();
            dto.setUserId(audit.getUserId());
            dto.setUserName(audit.getUserName());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserAuditResponseDto> getLatestDeletedUser() {
        List<UserAudit> audits = userAuditRepository.findTop5ByEventTypeOrderByCreatedAtDesc(EventType.DELETED);

        return audits.stream().map(audit -> {
            UserAuditResponseDto dto = new UserAuditResponseDto();
            dto.setUserId(audit.getUserId());
            dto.setUserName(audit.getUserName());
            dto.setCreatedAt(audit.getCreatedAt());
            dto.setDeletedAt(audit.getDeletedAt());
            dto.setEventType(audit.getEventType());

            return dto;
        }).collect(Collectors.toList());
    }
}

