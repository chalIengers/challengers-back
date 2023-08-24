package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.entity.ClubJoin;
import org.knulikelion.challengers_backend.data.entity.UserClub;

public interface ClubJoinService {

    public ClubJoin createJoinRequest(Long userId, Long clubId);

    public UserClub acceptJoinRequest(Long joinRequestId);
}
