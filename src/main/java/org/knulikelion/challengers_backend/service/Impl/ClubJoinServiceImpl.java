package org.knulikelion.challengers_backend.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubJoinResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.repository.ClubJoinRepository;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubJoinService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.Exception.ClubJoinNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.ClubNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ClubJoinRepository clubJoinRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDAO userDAO;
    private final ClubService clubService;

    @Override
    public ClubJoinResponseDto createJoinRequest(String token, Long clubId,String comment) {

        User user = userRepository.findById(userDAO.getByEmail(jwtTokenProvider.getUserEmail(token)).getId()).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        ClubJoin clubJoin = new ClubJoin(user, club, JoinRequestStatus.PENDING);
        clubJoin.setComments(comment);
        ClubJoin savedClubJoin = clubJoinRepository.save(clubJoin);
        return new ClubJoinResponseDto(savedClubJoin.getId(), userDAO.getByEmail(jwtTokenProvider.getUserEmail(token)).getId(), clubId,JoinRequestStatus.PENDING);
    }

    @Override
    public String getJoinRequestComment(Long requestId) {
        ClubJoin clubJoin = clubJoinRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid request ID:" + requestId));
        if(clubJoin.getStatus()!=JoinRequestStatus.PENDING) {
            throw new IllegalArgumentException("The request is not pending");
        }
        return clubJoin.getComments();
    }

    @Override
    public BaseResponseDto acceptJoinRequest(Long clubId, String userEmail, String addUserEmail) {
        User user = userRepository.getByEmail(userEmail);
        Club club = clubRepository.getById(clubId);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if (!club.getClubManager().equals(user)){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽 멤버 수락 권한이 없습니다.");
        }else {
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
            baseResponseDto.setMsg("클럽 멤버 수락 권한이 없습니다.");
        }else {
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
    public List<PendingUserResponseDto> getPendingRequestUser(Club club) {

        List<ClubJoin> clubJoins = club.getClubJoins();
        List<PendingUserResponseDto> pendingUsers = new ArrayList<>();

        for(ClubJoin clubJoin : clubJoins) {
            if(clubJoin.getStatus() == JoinRequestStatus.PENDING) {
                User user = clubJoin.getUser();
                PendingUserResponseDto pendingUser = new PendingUserResponseDto(user.getId(),user.getUserName(), user.getEmail());
                pendingUsers.add(pendingUser);
            }
        }
        return pendingUsers;
    }
}
