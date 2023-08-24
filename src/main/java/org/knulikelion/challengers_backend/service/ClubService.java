package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubListResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;

import java.util.List;
import java.util.Optional;

public interface ClubService {
    Optional<Club> findById(Long id);
    Object getClubById(Long id);
    ResultResponseDto removeClub(Long id);
    ResultResponseDto createClub(ClubCreateRequestDto clubCreateRequestDto);
    ResultResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception;
    ResultResponseDto updateMember(Long findUserId, Long updateUserId, Long clubId);
    ResultResponseDto removeMember(Long findUserId, Long clubId);
    ResultResponseDto addMember(Long userId, Long clubId);
    List<ClubListResponseDto> findAllClubs();
}
