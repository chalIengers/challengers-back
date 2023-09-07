package org.knulikelion.challengers_backend.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.repository.ClubJoinRepository;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubJoinService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.Exception.ClubNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.UnauthorizedException;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRepository clubJoinRepository;
    private final UserClubRepository userClubRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClubService clubService;

    @Override
    public BaseResponseDto createJoinRequest(String token, Long clubId, String comment) {

        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        User user = userRepository.findByEmail(jwtTokenProvider.getUserEmail(token));

        List<Club> foundClub = clubRepository.findAllByClubManager(user);
        for(Club temp : foundClub) {
            if(temp.getId().equals(clubId)) {
                return BaseResponseDto.builder()
                        .success(false)
                        .msg("클럽 생성자는 가입을 요청할 수 없습니다.")
                        .build();
            }
        }

        List<UserClub> userClubList = userClubRepository.findByUserId(user.getId());

        for(UserClub userClub : userClubList) {
            if(userClub.getClub().getId().equals(clubId)) {
                return BaseResponseDto.builder()
                        .success(false)
                        .msg("이미 가입된 클럽입니다.")
                        .build();
            }
        }

        if (user == null) {
            throw new UserNotFoundException("User with email not found");
        }

        ClubJoin checkRequest = clubJoinRepository.findByClubIdAndUserId(clubId, user.getId());
        if (checkRequest == null) {
            ClubJoin clubJoin = new ClubJoin(user, club, JoinRequestStatus.PENDING);
            clubJoin.setComments(comment);

            clubJoinRepository.save(clubJoin);

            return BaseResponseDto.builder()
                    .success(true)
                    .msg("클럽 가입 요청 완료.")
                    .build();
        } else {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("중복된 요청입니다.")
                    .build();
        }
    }

    @Override
    public BaseResponseDto acceptJoinRequest(Long clubId, String userEmail, String addUserEmail) {
        User user = userRepository.getByEmail(userEmail);
        Club club = clubRepository.getById(clubId);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if (!club.getClubManager().equals(user)) {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽 멤버 수락 권한이 없습니다.");
        } else {
            User findUser = userRepository.getByEmail(addUserEmail);
            ClubJoin clubJoin = clubJoinRepository.findByClubIdAndUserId(clubId, findUser.getId());
            clubJoin.setClub(null);
            clubJoin.setUser(null);

            clubJoinRepository.delete(clubJoin);

            log.info("clubService [addMember] {} to {}", findUser.getEmail(), club.getClubName());
            baseResponseDto = clubService.addMember(findUser.getId(), club.getId());
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto rejectJoinRequest(Long clubId, String userEmail, String rejectUserEmail) {
        User user = userRepository.findByEmail(userEmail);
        User findUser = userRepository.findByEmail(rejectUserEmail);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if (!club.getClubManager().equals(user)) {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽 멤버 거절 권한이 없습니다.");
        } else {
            ClubJoin clubJoin = clubJoinRepository.findByClubIdAndUserId(clubId, findUser.getId());
            clubJoin.setClub(null);
            clubJoin.setUser(null);
            clubJoinRepository.delete(clubJoin);
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("클럽 멤버 합류를 거절하였습니다.");
        }
        return baseResponseDto;
    }

    @Override
    public List<PendingUserResponseDto> getPendingRequestUser(String userEmail, Long clubId) {

        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }

        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        if(club.getClubManager().equals(user)) {
            List<ClubJoin> clubJoins = club.getClubJoins();
            List<PendingUserResponseDto> pendingUsers = new ArrayList<>();

            for (ClubJoin clubJoin : clubJoins) {
                if (clubJoin.getStatus() == JoinRequestStatus.PENDING) {
                    User findUser = clubJoin.getUser();
                    PendingUserResponseDto pendingUser = new PendingUserResponseDto(findUser.getUserName(), findUser.getEmail(), clubJoin.getComments());
                    pendingUsers.add(pendingUser);
                }
            }
            return pendingUsers;
        }else{
            throw new UnauthorizedException("클럽 매니저 권한이 없습니다.");
        }
    }
}
