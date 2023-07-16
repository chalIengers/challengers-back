package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ClubService {
    Object getClubById(Long id);
    ResultResponseDto createClub();
    ResultResponseDto removeClub(Long id);
}
