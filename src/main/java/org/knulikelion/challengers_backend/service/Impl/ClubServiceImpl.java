package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.service.ClubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class ClubServiceImpl implements ClubService {
    private static final Logger logger = LoggerFactory.getLogger(ClubServiceImpl.class);
    private final ClubDAO clubDAO;

    public ClubServiceImpl(ClubDAO clubDAO) {
        this.clubDAO = clubDAO;
    }

    @Override
    public Object getClubById(Long id) {
        if (clubDAO.selectClubById(id).isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("클럽이 존재하지 않음");

            return resultResponseDto;
        }else{
            Club selectedClub = clubDAO.selectClubById(id).get();
            ClubResponseDto clubResponseDto = new ClubResponseDto();
            clubResponseDto.setId(selectedClub.getId());
            clubResponseDto.setClubName(selectedClub.getClubName());
            clubResponseDto.setLogoUrl(selectedClub.getLogoUrl());
            clubResponseDto.setClubDescription(selectedClub.getClubDescription());
            clubResponseDto.setClubForm(selectedClub.getClubForm());
            clubResponseDto.setClubApproved(selectedClub.getClubApproved());
            if(clubDAO.getUsersByClubId(id) == null){
                clubResponseDto.setClubMembers(null);
            }else{
                clubResponseDto.setClubMembers(clubDAO.getUsersByClubId(id));
            }
            return clubResponseDto;
        }
    }

    @Override
    public ResultResponseDto removeClub(Long id) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if(clubDAO.selectClubById(id).isEmpty()){
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("클럽이 존재하지 않음");
        }else{
            clubDAO.removeClub(id);
        }
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("클럽 삭제 됨");
        return resultResponseDto;
    }

    @Override
    public ResultResponseDto createClub(ClubRequestDto clubRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        Club club = new Club();
        club.setClubName(clubRequestDto.getClubName());
        club.setLogoUrl(clubRequestDto.getLogoUrl());
        club.setClubDescription(clubRequestDto.getClubDescription());
        club.setClubForm(clubRequestDto.getClubForm());
        club.setClubApproved(clubRequestDto.getClubApproved());
        club.setCreatedAt(currentTime);
        club.setUpdatedAt(currentTime);
        club.setUsers(null);
        clubDAO.createClub(club);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("클럽 생성 완료");
        return resultResponseDto;
    }

    @Override
    public ResultResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<Club> clubOptional = clubDAO.selectClubById(id);
        if(clubOptional.isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("클럽이 존재하지 않습니다.");
            return resultResponseDto;
        }

        Club club = clubOptional.get();
        club.setClubName(clubRequestDto.getClubName());
        club.setLogoUrl(clubRequestDto.getLogoUrl());
        club.setClubApproved(clubRequestDto.getClubApproved());
        club.setClubDescription(clubRequestDto.getClubDescription());
        club.setClubForm(clubRequestDto.getClubForm());
        club.setUpdatedAt(currentTime);
        clubDAO.updateClub(id, club);
        return null;
    }
}
