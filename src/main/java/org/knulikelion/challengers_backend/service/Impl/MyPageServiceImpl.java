package org.knulikelion.challengers_backend.service.Impl;


import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dto.request.ChangePasswordRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ChangePasswordWithCodeRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.UnregisterValidateResponseDto;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.enums.EventType;
import org.knulikelion.challengers_backend.data.repository.*;
import org.knulikelion.challengers_backend.service.MailService;
import org.knulikelion.challengers_backend.data.dto.request.UserRemoveRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.knulikelion.challengers_backend.service.MyPageService;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClubRepository clubRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final MailService mailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserAuditRepository userAuditRepository;
    private final ClubJoinRepository clubJoinRepository;

    public MyPageServiceImpl(UserRepository userRepository,
                             UserClubRepository userClubRepository,
                             PasswordEncoder passwordEncoder,
                             ClubRepository clubRepository,
                             ProjectRepository projectRepository,
                             ProjectService projectService,
                             EmailVerificationRepository emailVerificationRepository,
                             MailService mailService, UserAuditRepository userAuditRepository, ClubJoinRepository clubJoinRepository) {
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
        this.passwordEncoder = passwordEncoder;
        this.clubRepository = clubRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailService = mailService;
        this.userAuditRepository = userAuditRepository;
        this.clubJoinRepository = clubJoinRepository;
    }

    @Override
    public MyPageResponseDto getInfo(String email) {
        User user = userRepository.getByEmail(email);
        List<UserClub> userClub = userClubRepository.findByUserId(user.getId());

        MyPageResponseDto myPageResponseDto = new MyPageResponseDto();

        myPageResponseDto.setName(user.getUserName());
        myPageResponseDto.setEmail(user.getUsername());

        List<String> clubs = new ArrayList<>();

        for (UserClub temp : userClub) {
            clubs.add(temp.getClub().getClubName());
        }

        myPageResponseDto.setClubs(clubs);

        return myPageResponseDto;
    }

    @Override
    public BaseResponseDto changePassword(String email, ChangePasswordWithCodeRequestDto changePasswordWithCodeRequestDto) {
        User user = userRepository.findByEmail(email);

        if(checkPassword(email, changePasswordWithCodeRequestDto.getUserPw())){ /*입력한 비밀번호가 일치할 때*/

            if(!passwordEncoder.matches(changePasswordWithCodeRequestDto.getChangePw(), user.getPassword())){ /*바꾸려는 비밀번호가 일치하지 않을 때*/
                EmailVerification checkNumber = emailVerificationRepository.findByEmail(email).get(0);

                if(!LocalDateTime.now().isAfter(checkNumber.getExpireTime())){ /*인증번호가 만료되지 않았을 때*/

                    if(checkNumber.getCode().equals(changePasswordWithCodeRequestDto.getApprovalNumber())){ /*인증번호가 일치 할 때*/
                        user.setPassword(passwordEncoder.encode(changePasswordWithCodeRequestDto.getChangePw()));
                        userRepository.save(user);

                        emailVerificationRepository.delete(checkNumber);
                        return BaseResponseDto.builder()
                                .success(true)
                                .msg("성공적으로 비밀번호를 변경하였습니다.")
                                .build();
                    }else{ /*인증번호가 틀렸을 때*/
                        return BaseResponseDto.builder()
                                .success(false)
                                .msg("인증번호가 틀렸습니다. 입력해주세요.")
                                .build();
                    }
                }else{ /*인증번호가 만료 되었을 때*/
                    emailVerificationRepository.delete(checkNumber);
                    return BaseResponseDto.builder()
                            .success(false)
                            .msg("인증번호가 만료되었습니다. 재발급 해주세요.")
                            .build();
                }
            }else{ /*변경하려는 비밀번호가 같을 때*/
                return BaseResponseDto.builder()
                        .success(false)
                        .msg("변경하시려는 비밀번호가 같습니다. 다시 입력해주세요.")
                        .build();
            }
        }else{ /*입력한 비밀번호가 틀렸을 때*/
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("비밀번호가 틀렸습니다. 다시 입력해주세요.")
                    .build();
        }

    }

    @Override
    public BaseResponseDto sendPwChangeCode(String email, ChangePasswordRequestDto changePasswordRequestDto) {
        User user = userRepository.findByEmail(email);

        if(checkPassword(email,changePasswordRequestDto.getUserPw())) { /*유저 비밀번호가 일치 했을 때*/

            if (!passwordEncoder.matches(changePasswordRequestDto.getChangePw(), user.getPassword())) { /*바꾸려는 비밀번호가 다를 때*/

                List<EmailVerification> checkEmail = emailVerificationRepository.findByEmail(email);
                if (!checkEmail.isEmpty()) {
                    for (EmailVerification temp : checkEmail) {
                        emailVerificationRepository.delete(temp);
                    }
                }

                log.info("[sendPwCode] 인증 번호 전송 Email : {}", email);
                String approvalNumber = mailService.sendPwMail(email);
                log.info("[sendCode] 인증 번호 전송 완료 Email : {}, number : {}", email, approvalNumber);

                EmailVerification emailVerification = EmailVerification.builder()
                        .email(email)
                        .code(approvalNumber)
                        .expireTime(LocalDateTime.now().plusMinutes(3)) /*현재 시간으로부터 5분 뒤*/
                        .build();

                emailVerificationRepository.save(emailVerification);
                log.info("[sendPwCode] DataBase 저장 완료");

                return BaseResponseDto.builder()
                        .success(true)
                        .msg("성공적으로 인증 번호를 전송하였습니다.")
                        .build();
            }else{ /*바꾸려는 비밀번호가 현재 비밀번호랑 동일할 때*/
                return BaseResponseDto.builder()
                        .success(false)
                        .msg("입력하신 비밀번호가 같습니다. 다른 비밀번호를 입력해주세요.")
                        .build();
            }
        }else{ /*입력한 비밀번호가 틀렸을 때*/
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("비밀번호가 틀렸습니다. 다시 입력해주세요.")
                    .build();
        }
    }

    @Override
    public Boolean checkPassword(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        if(passwordEncoder.matches(password, user.getPassword())){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public UnregisterValidateResponseDto validateUnRegister(String email) {
        UnregisterValidateResponseDto unregisterValidateResponseDto = new UnregisterValidateResponseDto();
        User user = userRepository.getByEmail(email);
        List<Club> clubList = clubRepository.findAllByClubManager(user);

        unregisterValidateResponseDto.setSuccess(false);
        unregisterValidateResponseDto.setValidateUser(true);
        unregisterValidateResponseDto.setNotAdministrator(true);
        unregisterValidateResponseDto.setMemberEmpty(true);
        unregisterValidateResponseDto.setMatchPassword(true);

        if(user == null) {
            unregisterValidateResponseDto.setValidateUser(false);
        }

//        운영 중인 클럽에 본인 제외 다른 멤버가 존재하는지 체크
        for (Club club : clubList) {
            List<UserClub> isMember = userClubRepository.findAllByClubId(club.getId());
            for (UserClub userClub : isMember) {
                if(!userClub.getUser().getId().equals(user.getId())) {
                    unregisterValidateResponseDto.setMemberEmpty(false);
                }
            }
        }

//        관리자 권한을 가진 사용자인지 체크
        if(user.getRoles().contains("ROLE_ADMIN")) {
            unregisterValidateResponseDto.setNotAdministrator(false);
        }

        if(unregisterValidateResponseDto.isValidateUser() && unregisterValidateResponseDto.isMatchPassword()
        && unregisterValidateResponseDto.isNotAdministrator() && unregisterValidateResponseDto.isMemberEmpty()) {
            unregisterValidateResponseDto.setSuccess(true);
        } else {
            unregisterValidateResponseDto.setSuccess(false);
        }

        return unregisterValidateResponseDto;
    }

    @Override
    public UnregisterValidateResponseDto unRegister(String email, UserRemoveRequestDto userRemoveRequestDto) {
        User user = userRepository.getByEmail(email);
        String userName = user.getUserName();
        UnregisterValidateResponseDto unregisterValidateResponseDto = new UnregisterValidateResponseDto();
        unregisterValidateResponseDto.setValidateUser(true);
        unregisterValidateResponseDto.setNotAdministrator(true);
        unregisterValidateResponseDto.setMemberEmpty(true);
        unregisterValidateResponseDto.setMatchPassword(true);

//        존재하는 사용자인지 확인
        if(user == null) {
            unregisterValidateResponseDto.setValidateUser(false);
        }

//        회원 탈퇴를 진행하기 위한 현재 비밀번호 체크
        if(!passwordEncoder.matches(userRemoveRequestDto.getPassword(), user.getPassword())) {
            unregisterValidateResponseDto.setMatchPassword(false);
        }

        List<Club> clubList = clubRepository.findAllByClubManager(user);

//        본인이 운영하고 있는 클럽이 존재하는지 체크
        for (Club club : clubList) {
            List<UserClub> isMember = userClubRepository.findAllByClubId(club.getId());
            for (UserClub userClub : isMember) {
                if(!userClub.getUser().getId().equals(user.getId())) {
                    unregisterValidateResponseDto.setMemberEmpty(false);
                }
            }
        }

        if(!clubList.isEmpty()) {
            for (Club temp : clubList) {
                clubRepository.deleteById(temp.getId());
            }
        }

//        관리자 권한을 가진 사용자인지 체크
        if(user.getRoles().contains("ROLE_ADMIN")) {
            unregisterValidateResponseDto.setNotAdministrator(false);
        }

        List<Project> projectList = projectRepository.findAllByUser(user);

        if(unregisterValidateResponseDto.isNotAdministrator() && unregisterValidateResponseDto.isMatchPassword()
        && unregisterValidateResponseDto.isValidateUser() && unregisterValidateResponseDto.isMemberEmpty()) {
//        본인이 업로드한 프로젝트를 모두 삭제
            if(!projectList.isEmpty()) {
                for (Project project : projectList) {
                    projectService.removeProject(project.getId());
                }
            }

//        다른 클럽에 소속되어 있다면 멤버에서 자동으로 추방
            List<UserClub> userClubList = userClubRepository.findByUserId(user.getId());
            if(!userClubList.isEmpty()) {
                userClubRepository.deleteAll(userClubList);
            }

            /*클럽 가입 요청 다 삭제*/
            List<ClubJoin> clubJoinList = clubJoinRepository.findAllByUserId(user.getId());
            if(!clubJoinList.isEmpty()){
                clubJoinRepository.deleteAll(clubJoinList);
            }

            /*이메일 인증시 저장한 {사용자 이메일 : 이메일 인증번호} 정보 삭제*/
            List<EmailVerification> emailVerificationList = emailVerificationRepository.findByEmail(email);
            if(!emailVerificationList.isEmpty()){
                emailVerificationRepository.deleteAll(emailVerificationList);
            }

//        사용자의 모든 권한을 비활성화
            user.setUseAble(false);
            userRepository.save(user);
            unregisterValidateResponseDto.setSuccess(true);
        }

//        유저 삭제 기록 저장.
        UserAudit audit = new UserAudit();
        audit.setUserId(user.getId());
        audit.setUserName(userName);
        audit.setEventType(EventType.DELETED);
        audit.setCreatedAt(user.getCreatedAt());
        audit.setDeletedAt(LocalDateTime.now());

        userAuditRepository.save(audit);

//        회원 탈퇴 프로세스 완료
        return unregisterValidateResponseDto;
    }

}
