package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/club")
public class ClubController {
    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }
    @GetMapping("/get")
    public Object getClubById(Long id){
        Object result = clubService.getClubById(id);
        return result;
    }
    @DeleteMapping("/remove")
    public ResultResponseDto removeClubById(Long id){
        return clubService.removeClub(id);
    }
    @PostMapping("/create")
    public ResultResponseDto createClub(@RequestBody ClubCreateRequestDto clubCreateRequestDto){
        return clubService.createClub(clubCreateRequestDto);
    }
    @PutMapping("/update")
    public ResultResponseDto updateClub(@RequestBody ClubRequestDto clubRequestDto , Long clubId) throws Exception {
        return clubService.updateClub(clubId,clubRequestDto);
    }
    @PostMapping("/addMember")
    public ResultResponseDto addClubMember(Long userId, Long clubId){
        return clubService.addMember(userId,clubId);
    }
    @PutMapping("/updateMember")
    public ResultResponseDto updateClubMember(Long userId, Long updateUserId, Long clubId){
        return clubService.updateMember(userId,updateUserId,clubId);
    }
    @DeleteMapping("/deleteMember")
    public ResultResponseDto removeClubMember(Long userId, Long clubId){
        return clubService.removeMember(userId,clubId);
    }

    @GetMapping("/clubList")
    public List<Club> clubList() {
        return clubService.findAllClub();
    }

}
