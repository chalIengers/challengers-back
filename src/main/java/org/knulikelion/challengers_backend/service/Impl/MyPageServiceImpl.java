package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dto.request.UserRemoveRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.ProjectRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.knulikelion.challengers_backend.service.MyPageService;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyPageServiceImpl implements MyPageService {
    private final UserRepository userRepository;
    private final UserClubRepository userClubRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClubRepository clubRepository;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;

    public MyPageServiceImpl(UserRepository userRepository,
                             UserClubRepository userClubRepository,
                             PasswordEncoder passwordEncoder,
                             ClubRepository clubRepository,
                             ProjectRepository projectRepository,
                             ProjectService projectService) {
        this.userRepository = userRepository;
        this.userClubRepository = userClubRepository;
        this.passwordEncoder = passwordEncoder;
        this.clubRepository = clubRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
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
    public BaseResponseDto unRegister(String email, UserRemoveRequestDto userRemoveRequestDto) {
        User user = userRepository.getByEmail(email);

//        해당 사용자가 존재하는 사용자인지 체크
        if(user == null) {
            throw new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }

//        회원 탈퇴를 진행하기 위한 현재 비밀번호 체크
        if(!passwordEncoder.matches(userRemoveRequestDto.getPassword(), user.getPassword())) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("사용자의 비밀번호가 일치하지 않습니다.")
                    .build();
        }

        List<Club> clubList = clubRepository.findAllByClubManager(user);

//        운영 중인 클럽에 본인 제외 다른 멤버가 존재하는지 체크
        for (Club club : clubList) {
            List<UserClub> isMember = userClubRepository.findAllByClubId(club.getId());
            for (UserClub userClub : isMember) {
                if(!userClub.getUser().getId().equals(user.getId())) {
                    return BaseResponseDto.builder()
                            .success(false)
                            .msg("운영하고 있는 클럽에 다른 멤버가 존재합니다.")
                            .build();
                }
            }
        }

//        본인이 운영하고 있는 클럽이 존재하는지 체크
        if(!clubList.isEmpty()) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("운영 중인 클럽이 존재합니다.")
                    .build();
        }

//        관리자 권한을 가진 사용자인지 체크
        if(user.getRoles().contains("ROLE_ADMIN")) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("관리자 권한을 가진 사용자는 탈퇴할 수 없습니다.")
                    .build();
        }

        List<Project> projectList = projectRepository.findAllByUser(user);

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

//        사용자의 모든 권한을 비활성화
        user.setUseAble(false);
        userRepository.save(user);

//        회원 탈퇴 프로세스 완료
        return BaseResponseDto.builder()
                .success(true)
                .msg("회원 탈퇴가 완료되었습니다.")
                .build();
    }
}
