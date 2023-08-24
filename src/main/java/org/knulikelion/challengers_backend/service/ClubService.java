package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubListResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubLogoResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;

import java.util.List;
import java.util.Optional;

public interface ClubService {
    Optional<Club> findById(Long id);
    Object getClubById(Long id);
    BaseResponseDto removeClub(Long id);
    BaseResponseDto createClub(ClubCreateRequestDto clubCreateRequestDto);
    BaseResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception;
    BaseResponseDto updateMember(Long findUserId, Long updateUserId, Long clubId);
    BaseResponseDto removeMember(Long findUserId, Long clubId);
    BaseResponseDto addMember(Long userId, Long clubId);
    List<ClubLogoResponseDto> getAllClubLogo();
    List<ClubListResponseDto> findAllClubs();
}
