package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dto.response.AdminDisableUserManageResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.AdminUserManageResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.repository.*;
import org.knulikelion.challengers_backend.service.AdminUserManageService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdminUserManageServiceImpl implements AdminUserManageService {

    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;
    private final ClubRepository clubRepository;
    private final ClubService clubService;
    private final ClubJoinRepository clubJoinRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ProjectAuditRepository projectAuditRepository;
    private final ClubAuditRepository clubAuditRepository;
    @Autowired
    public AdminUserManageServiceImpl(UserRepository userRepository, UserClubRepository userClubRepository, ClubRepository clubRepository, ClubService clubService, ClubJoinRepository clubJoinRepository, ProjectRepository projectRepository, ProjectService projectService, EmailVerificationRepository emailVerificationRepository, ProjectAuditRepository projectAuditRepository, ClubAuditRepository clubAuditRepository) {
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
        this.clubRepository = clubRepository;
        this.clubService = clubService;
        this.clubJoinRepository = clubJoinRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
        this.emailVerificationRepository = emailVerificationRepository;
        this.projectAuditRepository = projectAuditRepository;
        this.clubAuditRepository = clubAuditRepository;
    }


    @Override
    public ResponseEntity<Page<AdminUserManageResponseDto>> getAllUseAbleUser(Pageable pageable) {
        /*사용자 계정이 활성화 된 계정만 resp*/
        Page<User> users = userRepository.findByUseAbleTrue(pageable);

        Page<AdminUserManageResponseDto> result = users.map(user -> {
            // 해당 유저의 UserClubs 조회
            List<UserClub> userClubs = userClubRepository.findByUserId(user.getId());

            if(!userClubs.isEmpty()) {
                // UserClubs 에서 Club 만 추출하여 리스트 생성
                List<Club> clubs = userClubs.stream()
                        .map(UserClub::getClub)
                        .toList();

                List<String> clubNameList = new ArrayList<>();
                for(Club temp : clubs) {
                    clubNameList.add(temp.getClubName());
                }

                return AdminUserManageResponseDto.builder()
                        .userName(user.getUserName())
                        .userEmail(user.getEmail())
                        .clubList(clubNameList)
                        .build();
            }else{
                return AdminUserManageResponseDto.builder()
                        .userName(user.getUserName())
                        .userEmail(user.getEmail())
                        .clubList(null)
                        .build();
            }
        });

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<AdminDisableUserManageResponseDto>> getAllDisAbleUser(Pageable pageable) {
        Page<User> disableUserList = userRepository.findByUseAbleFalse(pageable);

        Page<AdminDisableUserManageResponseDto> result = disableUserList.map(user->{
            return  AdminDisableUserManageResponseDto.builder()
                    .userName(user.getUserName())
                    .userEmail(user.getEmail())
                    .build();
        });

        return ResponseEntity.ok(result);
    }

    /*강제 삭제*/
    @Override
    public ResponseEntity<BaseResponseDto> unregisterUser(List<String> userEmailList) {

        for(String email : userEmailList){
            User findUser = userRepository.findByEmail(email);

            if(findUser == null){
                throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.");
            }

            if(!findUser.isUseAble()){
                return new ResponseEntity<>(
                        BaseResponseDto.builder()
                                .success(false)
                                .msg(findUser.getEmail()+ "은 이미 비활성화 된 계정입니다. 다시 확인해주세요.")
                                .build()
                        ,HttpStatus.BAD_REQUEST
                );
            }

            // 프로젝트 생성자라면 -> 삭제
            List<Project> projectList = projectRepository.findAllByUser(findUser);
            if(!projectList.isEmpty()){
                for(Project project : projectList){
                    projectService.removeProject(project.getId());

                }
            }

            // 클럽 생성자 -> 삭제
            List<Club> clubList= clubRepository.findAllByClubManager(findUser);
            if(!clubList.isEmpty()){
                for(Club club : clubList){
                    clubService.removeClub(club.getId());
                }
            }

            // 다른 클럽에 속해 있다면 -> 삭제
            List<UserClub> userClubList = userClubRepository.findByUserId(findUser.getId());
            if(!userClubList.isEmpty()){
                for(UserClub userClub : userClubList){
                    userClub.setClub(null);
                    userClub.setUser(null);
                    userClubRepository.delete(userClub);
                }
            }

            // 클럽 가입 요청 기록 삭제
            List<ClubJoin> clubJoinList = clubJoinRepository.findAllByUserId(findUser.getId());
            if(!clubJoinList.isEmpty()){
                for(ClubJoin clubJoin : clubJoinList){
                    clubJoin.setClub(null);
                    clubJoin.setUser(null);
                    clubJoinRepository.delete(clubJoin);
                }
            }

            // 이메일 인증 데이터 삭제
            List<EmailVerification> emailVerificationList = emailVerificationRepository.findByEmail(findUser.getEmail());
            if(!emailVerificationList.isEmpty()){
                for(EmailVerification emailVerification : emailVerificationList){
                    emailVerificationRepository.delete(emailVerification);
                }
            }

            findUser.setUseAble(false);
            userRepository.save(findUser);
        }
        return ResponseEntity.ok(BaseResponseDto.builder()
                .success(true)
                .msg("유저를 성공적으로 강제 삭제 하였습니다.")
                .build());
    }

    @Override
    public ResponseEntity<BaseResponseDto> reRegisterUser(List<String> userEmailList) {
        for(String email : userEmailList){
            User findUser = userRepository.findByEmail(email);

            if(findUser == null){
                throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.");
            }
            if(findUser.isUseAble()){
                return new ResponseEntity<>(
                        BaseResponseDto.builder()
                                .success(false)
                                .msg(findUser.getEmail() + " 은 이미 활성화 된 계정입니다. 다시 확인해주세요.")
                                .build()
                        , HttpStatus.BAD_REQUEST
                );
            }

            findUser.setUseAble(true);
            userRepository.save(findUser);
        }
        return ResponseEntity.ok(BaseResponseDto.builder()
                .success(true)
                .msg("해당 계정들을 정상적으로 활성화 하였습니다.")
                .build());
    }
}
