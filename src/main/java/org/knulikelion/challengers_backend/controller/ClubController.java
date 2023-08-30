package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.JoinRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.service.ClubJoinService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/club")
public class ClubController {
    private final ClubService clubService;
    private final ClubJoinService clubJoinService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ClubController(ClubService clubService, ClubJoinService clubJoinService, JwtTokenProvider jwtTokenProvider) {
        this.clubService = clubService;
        this.clubJoinService = clubJoinService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/get/logo/all")
    public List<ClubLogoResponseDto> getAllClubLogo() {
        return clubService.getAllClubLogo();
    }

    @GetMapping("/get/club/my")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public List<UserClubResponseDto> getUsersClub(HttpServletRequest request) {
        return clubService.getUsersClub(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")));
    }
  
    @GetMapping("/get")
    public Object getClubById(Long id){
        return clubService.getClubById(id);
    }

    @DeleteMapping("/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto removeClubById(Long id){
        return clubService.removeClub(id);
    }

    @PostMapping("/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto createClub(HttpServletRequest request, @RequestBody ClubCreateRequestDto clubCreateRequestDto){
        return clubService.createClub(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),clubCreateRequestDto);
    }

    @PutMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto updateClub(HttpServletRequest request,@RequestBody ClubRequestDto clubRequestDto) throws Exception {
        return clubService.updateClub(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),clubRequestDto);
    }
    
    @DeleteMapping("/deleteMember")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto removeClubMember(HttpServletRequest request,String userEmail, Long clubId){
        return clubService.removeMember(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),userEmail,clubId);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ClubListResponseDto> findAllClubs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return clubService.findAllClubs(page,size);
    }

    @GetMapping("/members/{clubId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<ClubMemberResponseDto>> getMembersByClub(@PathVariable Long clubId) {
        return ResponseEntity.ok(clubService.getMembersByClubId(clubId));
    }

    @PostMapping("/join-requests")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ClubJoinResponseDto createJoinRequest(HttpServletRequest request, @RequestBody JoinRequestDto joinRequest) {
        return clubJoinService.createJoinRequest(request.getHeader("X-AUTH-TOKEN"), joinRequest.getCludId(), joinRequest.getComment());
    }

    @GetMapping("/join-requests/comment/{requestId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<String> getJoinRequestComment(@PathVariable Long requestId, HttpServletRequest request) {
        String comment = clubJoinService.getJoinRequestComment(requestId,jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")));
        return ResponseEntity.ok(comment);
    }

    @PostMapping("/join-requests/accept/{clubId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto acceptJoinRequest(@PathVariable Long clubId, HttpServletRequest request,@RequestParam String addUserEmail){
        return clubJoinService.acceptJoinRequest(clubId,jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),addUserEmail);
    }
    @DeleteMapping("/join-requests/reject/{clubId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto rejectJoinRequest(@PathVariable Long clubId, HttpServletRequest request,@RequestParam String rejectUserEmail){
        return clubJoinService.rejectJoinRequest(clubId,jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),rejectUserEmail);
    }

    @GetMapping("/join-requests/pending/users/{clubId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<PendingUserResponseDto>> getPendingUsers(@PathVariable Long clubId) {
        Club club = clubService.findById(clubId)
                .orElseThrow(() -> new NoSuchElementException("클럽을 찾을 수 없습니다."));
        List<PendingUserResponseDto> getPendingUsers = clubJoinService.getPendingRequestUser(club);
        return ResponseEntity.ok(getPendingUsers);
    }

}
