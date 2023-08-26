package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.UserClub;
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
    public List<ClubLogoResponseDto> getAllClubLogo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return clubService.getAllClubLogo(page, size);
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

    @PostMapping("/addMember")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto addClubMember(Long userId, Long clubId){
        return clubService.addMember(userId,clubId);
    }
    
    @DeleteMapping("/deleteMember")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto removeClubMember(HttpServletRequest request,Long userId, Long clubId){
        return clubService.removeMember(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),userId,clubId);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ClubListResponseDto> findAllClubs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {
        return clubService.findAllClubs(page,size);
    }

    @PostMapping("/join-requests")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ClubJoinResponseDto createJoinRequest(HttpServletRequest request, @RequestParam Long clubId) {
        return clubJoinService.createJoinRequest(request.getHeader("X-AUTH-TOKEN"), clubId);
    }

    @PutMapping("/join-requests/{joinRequestId}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public UserClub acceptJoinRequest(@PathVariable Long joinRequestId, @RequestParam("isAccepted") boolean isAccepted){
        return clubJoinService.acceptJoinRequest(joinRequestId,isAccepted);
    }

    @GetMapping("/pending/requests/users/{clubId}")
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
