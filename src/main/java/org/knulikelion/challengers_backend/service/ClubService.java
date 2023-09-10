package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ClubService {
    Optional<Club> findById(Long id);
    BaseResponseDto removeClub(Long id);
    ResponseEntity<BaseResponseDto> createClub(String userEmail ,ClubCreateRequestDto clubCreateRequestDto);
    ResponseEntity<BaseResponseDto> updateClub(String userEmail,ClubRequestDto clubRequestDto) throws Exception;
    BaseResponseDto removeMember(String userEmail,String deleteUserEmail, Long clubId);
    BaseResponseDto addMember(Long userId, Long clubId);
    List<UserClubResponseDto> getUsersClub(String email);
    Object getClubById(Long id);
    ResponseEntity<ClubResponseDto> getClubDetailById(Long id);

    List<ClubLogoResponseDto> getAllClubLogo();
    Page<ClubListResponseDto> findAllClubs(int page, int size);
    ResponseEntity<List<ClubMemberResponseDto>> getMembersByClubId(Long clubId);
    ResponseEntity<BaseResponseDto> verifyCreateClub(String userEmail, ClubCreateRequestDto clubCreateRequestDto);
}
