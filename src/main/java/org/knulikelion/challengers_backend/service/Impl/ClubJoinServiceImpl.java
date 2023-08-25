package org.knulikelion.challengers_backend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.knulikelion.challengers_backend.data.dto.response.ClubJoinResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.data.repository.ClubJoinRepository;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubJoinService;
import org.knulikelion.challengers_backend.service.Exception.ClubJoinNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.ClubNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final ClubJoinRepository clubJoinRepository;

    @Override
    public ClubJoinResponseDto createJoinRequest(Long userId, Long clubId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        ClubJoin clubJoin = new ClubJoin(user, club, JoinRequestStatus.PENDING);
        ClubJoin savedClubJoin = clubJoinRepository.save(clubJoin);
        return new ClubJoinResponseDto(savedClubJoin.getId(),userId,clubId,JoinRequestStatus.PENDING);
    }

    @Override
    public UserClub acceptJoinRequest(Long joinRequestId, boolean isAccepted) {
        ClubJoin clubJoin = clubJoinRepository.findById(joinRequestId)
                .orElseThrow(ClubJoinNotFoundException::new);

        if(isAccepted) {
            clubJoin.setStatus(JoinRequestStatus.APPROVED);
            clubJoinRepository.save(clubJoin);

            UserClub userClub = new UserClub(clubJoin.getUser(),clubJoin.getClub());
            return userClubRepository.save(userClub);
        }else {
            clubJoin.setStatus(JoinRequestStatus.REJECTED);
            clubJoinRepository.save(clubJoin);
            return null;
        }
    }

    @Override
    public List<PendingUserResponseDto> getPendingRequestUser(Club club) {

        List<ClubJoin> clubJoins = club.getClubJoins();
        List<PendingUserResponseDto> pendingUsers = new ArrayList<>();

        for(ClubJoin clubJoin : clubJoins) {
            if(clubJoin.getStatus() == JoinRequestStatus.PENDING) {
                User user = clubJoin.getUser();
                PendingUserResponseDto pendingUser = new PendingUserResponseDto(user.getId(),user.getUsername(), user.getEmail());
                pendingUsers.add(pendingUser);
            }
        }
        return pendingUsers;
    }
}
