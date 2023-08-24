package org.knulikelion.challengers_backend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.ClubJoin;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.ClubJoinRepository;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubJoinService;
import org.knulikelion.challengers_backend.service.Exception.ClubJoinNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.ClubNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubJoinServiceImpl implements ClubJoinService {

    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final ClubJoinRepository clubJoinRepository;

    @Override
    public ClubJoin createJoinRequest(Long userId, Long clubId) {

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        ClubJoin clubJoin = new ClubJoin(user, club, false);
        return clubJoinRepository.save(clubJoin);
    }

    @Override
    public UserClub acceptJoinRequest(Long joinRequestId) {
        ClubJoin clubJoin = clubJoinRepository.findById(joinRequestId)
                .orElseThrow(ClubJoinNotFoundException::new);

        clubJoin.setAccepted(true);
        clubJoinRepository.save(clubJoin);

        UserClub userClub = new UserClub(clubJoin.getUser(),clubJoin.getClub());
        return userClubRepository.save(userClub);
    }
}
