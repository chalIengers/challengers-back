package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ClubServiceImpl implements ClubService {
    private static final Logger logger = LoggerFactory.getLogger(ClubServiceImpl.class);
    private final ClubDAO clubDAO;
    private final ClubRepository clubRepository;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;


    public ClubServiceImpl(ClubDAO clubDAO, ClubRepository clubRepository, UserClubRepository userClubRepository, UserRepository userRepository, UserDAO userDAO) {
        this.clubDAO = clubDAO;
        this.clubRepository = clubRepository;
        this.userClubRepository = userClubRepository;
        this.userRepository = userRepository;
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
            if(!clubDAO.getUsersByClubId(id).isEmpty()){
                clubResponseDto.setClubMembers(clubDAO.getUsersByClubId(id));
            }else{
                clubResponseDto.setClubMembers(null);
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
//            UserClub 매핑 값 삭제
            List<UserClub> selectedMapping = userClubRepository.findAllByClubId(id);
            for (UserClub userClub : selectedMapping) {
                userClub.setUser(null);
                userClub.setClub(null);
                userClubRepository.delete(userClub);
            }
//            클럽 삭제
            clubRepository.deleteById(id);
//            List<UserClub> userClubList =userClubRepository.findAll();
//            for(UserClub userClub : userClubList){
//                if(userClub.getClub().getId().equals(id)){
//                    userClub.setUser(null);
//                    userClub.setClub(null);
//                    userClubRepository.save(userClub);
//                    clubDAO.removeClub(id);
//                    userClubRepository.delete(userClub);
//                }else{
//                    clubDAO.removeClub(id);
//                }
//            }
        }
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("클럽 삭제 됨");
        return resultResponseDto;
    }

    @Override
    public ResultResponseDto createClub(ClubCreateRequestDto clubCreateRequestDto) {

        Club club = new Club();
        club.setClubName(clubCreateRequestDto.getClubName());
        club.setLogoUrl(clubCreateRequestDto.getLogoUrl());
        club.setClubDescription(clubCreateRequestDto.getClubDescription());
        club.setClubForm(clubCreateRequestDto.getClubForm());
        club.setClubApproved(0);
        club.setCreatedAt(LocalDateTime.now());
        club.setUpdatedAt(LocalDateTime.now());

        clubDAO.createClub(club);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("클럽 생성 완료");
        return resultResponseDto;
    }

    @Override
    public ResultResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception {

        Optional<Club> clubOptional = clubDAO.selectClubById(id);
        if(clubOptional.isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("클럽이 존재하지 않습니다.");
            return resultResponseDto;
        }else {
            Club club = clubOptional.get();
            club.setClubName(clubRequestDto.getClubName());
            club.setLogoUrl(clubRequestDto.getLogoUrl());
            club.setClubDescription(clubRequestDto.getClubDescription());
            club.setClubForm(clubRequestDto.getClubForm());
            club.setClubApproved(clubRequestDto.getClubApproved());
            club.setUpdatedAt(LocalDateTime.now());

            clubDAO.updateClub(id, club);
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("클럽 생성 완료");
            return resultResponseDto;
        }
    }

    @Override
    public ResultResponseDto updateMember(Long findUserId, Long updateUserId, Long clubId) {
        List<UserClub> userClubList = userClubRepository.findAll();
        User updateUser = userRepository.getById(updateUserId);
        Club club = clubRepository.getById(clubId);
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        boolean isUpdated = false;
        for (UserClub userClub : userClubList) {
            if (userClub.getClub().getId().equals(clubId) && userClub.getUser().getId().equals(findUserId)) {
                userClub.setUser(updateUser);
                userClub.setClub(club);
                userClubRepository.save(userClub);
                isUpdated = true;
                break;
            }
        }

        if (isUpdated) {
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("클럽 멤버 업데이트 완료");
        } else {
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("클럽 멤버 업데이트 실패");
        }

        return resultResponseDto;
    }

    @Override
    public ResultResponseDto removeMember(Long findUserId, Long clubId) {
        List<UserClub> userClubList = userClubRepository.findAll();
        Long userClubId = null;
        for (UserClub userClub : userClubList){
            if((userClub.getClub().getId().equals(clubId)) && (userClub.getUser().getId().equals(findUserId))){
                userClubId = userClub.getId();
                break;
            }
        }
        if(userClubId!=null){
            userClubRepository.deleteById(userClubId);
        }
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("멤버 삭제 완료");

        return resultResponseDto;
    }

    @Override
    public ResultResponseDto addMember(Long userId, Long clubId) {
        User user = userRepository.getById(userId);
        if (user.getId()==null){
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("해당 유저 없음");
            return  resultResponseDto;
        }else{
            Club club = clubRepository.getById(clubId);

            UserClub userClub = new UserClub();
            userClub.setClub(club);
            userClub.setUser(user);
            userClubRepository.save(userClub);

            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("멤버 추가");
            return resultResponseDto;
        }

    }

}
