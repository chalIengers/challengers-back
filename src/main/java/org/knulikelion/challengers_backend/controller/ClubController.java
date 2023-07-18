package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.service.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResultResponseDto createClub(@RequestBody ClubRequestDto clubRequestDto){
        return clubService.createClub(clubRequestDto);
    }
    @PutMapping("/update")
    public ResultResponseDto updateClub(@RequestBody ClubRequestDto clubRequestDto , Long clubId) throws Exception {
        return clubService.updateClub(clubId,clubRequestDto);
    }
}
