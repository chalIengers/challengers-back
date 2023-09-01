package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.PendingUserResponseDto;

import java.util.List;

public interface ClubJoinService {

    BaseResponseDto createJoinRequest(String token, Long clubId, String comment);

    BaseResponseDto acceptJoinRequest(Long clubId, String userEmail, String addUserEmail);

    BaseResponseDto rejectJoinRequest(Long clubId, String userEmail, String rejectUserEmail);

    List<PendingUserResponseDto> getPendingRequestUser(String userEmail, Long clubId);
}
