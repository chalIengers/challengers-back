package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.response.ClubJoinResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.ClubJoin;
import org.knulikelion.challengers_backend.data.entity.UserClub;

import java.util.List;

public interface ClubJoinService {

    public ClubJoinResponseDto createJoinRequest(Long userId, Long clubId);

    public UserClub acceptJoinRequest(Long joinRequestId);

    List<PendingUserResponseDto> getPendingRequestUser(Club club);
}
