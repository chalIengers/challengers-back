package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.ClubJoin;
import org.knulikelion.challengers_backend.data.entity.UserClub;

import java.util.List;

public interface ClubJoinService {

    public ClubJoin createJoinRequest(Long userId, Long clubId);

    public UserClub acceptJoinRequest(Long joinRequestId);

    List<ClubJoin> getPendingRequestUser(Club club);
}
