package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubJoinResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;

import java.util.List;

public interface ClubJoinService {

    ClubJoinResponseDto createJoinRequest(String token, Long clubId, String comment);

    String getJoinRequestComment(Long requestId,String UserEmail);

    BaseResponseDto acceptJoinRequest(Long clubId, String userEmail, String addUserEmail);

    BaseResponseDto rejectJoinRequest(Long clubId, String userEmail, String rejectUserEmail);

    List<PendingUserResponseDto> getPendingRequestUser(Club club);
}
