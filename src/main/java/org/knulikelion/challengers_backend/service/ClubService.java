package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ClubService {
    Object getClubById(Long id);
    ResultResponseDto removeClub(Long id);
    ResultResponseDto createClub(ClubRequestDto clubRequestDto);
    ResultResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception;
}
