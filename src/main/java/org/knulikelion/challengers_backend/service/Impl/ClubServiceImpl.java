package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubListResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubLogoResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubResponseDto;
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
import java.util.stream.Collectors;

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
    public Optional<Club> findById(Long id) {
        return clubRepository.findById(id);
    }

    @Override
    public Object getClubById(Long id) {
        if (clubDAO.selectClubById(id).isEmpty()){
            BaseResponseDto baseResponseDto = new BaseResponseDto();

            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽이 존재하지 않음");
            return baseResponseDto;

        }else{
            Club selectedClub = clubDAO.selectClubById(id).get();
            ClubResponseDto clubResponseDto = new ClubResponseDto();
            clubResponseDto.setId(selectedClub.getId());
            clubResponseDto.setClubName(selectedClub.getClubName());
            clubResponseDto.setLogoUrl(selectedClub.getLogoUrl());
            clubResponseDto.setClubDescription(selectedClub.getClubDescription());
            clubResponseDto.setClubForm(selectedClub.getClubForm());
            clubResponseDto.setClubApproved(selectedClub.getClubApproved());
            clubResponseDto.setCreatedAt(String.valueOf(selectedClub.getCreatedAt()));
            clubResponseDto.setUpdatedAt(String.valueOf(selectedClub.getUpdatedAt()));
            if(!clubDAO.getUsersByClubId(id).isEmpty()){
                clubResponseDto.setClubMembers(clubDAO.getUsersByClubId(id));
            }else{
                clubResponseDto.setClubMembers(null);
            }
            return clubResponseDto;
        }
    }

  @Override
  public List<ClubLogoResponseDto> getAllClubLogo() {
        List<ClubLogoResponseDto> clubLogoResponseDtoList = new ArrayList<>();
        List<Club> club = clubDAO.getAllClub();

        for(Club temp : club) {
            ClubLogoResponseDto clubLogoResponseDto = new ClubLogoResponseDto();
            if(!temp.getLogoUrl().isEmpty()) {
                clubLogoResponseDto.setLogoUrl(temp.getLogoUrl());
                clubLogoResponseDtoList.add(clubLogoResponseDto);
            }
        }

        return clubLogoResponseDtoList;
    }
    

    @Override
    public BaseResponseDto removeClub(Long id) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if(clubDAO.selectClubById(id).isEmpty()){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽이 존재하지 않음");
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
        }
        baseResponseDto.setSuccess(true);
        baseResponseDto.setMsg("클럽 삭제 됨");
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto createClub(ClubCreateRequestDto clubCreateRequestDto) {

        Club club = new Club();
        club.setClubName(clubCreateRequestDto.getClubName());
        club.setLogoUrl(clubCreateRequestDto.getLogoUrl());
        club.setClubDescription(clubCreateRequestDto.getClubDescription());
        club.setClubForm(clubCreateRequestDto.getClubForm());
        club.setClubApproved(0);
        club.setCreatedAt(LocalDateTime.now());
        club.setUpdatedAt(LocalDateTime.now());

        clubDAO.createClub(club);

        BaseResponseDto baseResponseDto = BaseResponseDto.builder()
                .success(true)
                .msg("클럽 생성이 완료되었습니다.")
                .build();

        return baseResponseDto;
    }

    @Override
    public BaseResponseDto updateClub(Long id, ClubRequestDto clubRequestDto) throws Exception {

        Optional<Club> clubOptional = clubDAO.selectClubById(id);
        if(clubOptional.isEmpty()){
            BaseResponseDto baseResponseDto = new BaseResponseDto();
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽이 존재하지 않습니다.");
            return baseResponseDto;
        }else {
            Club club = clubOptional.get();
            club.setClubName(clubRequestDto.getClubName());
            club.setLogoUrl(clubRequestDto.getLogoUrl());
            club.setClubDescription(clubRequestDto.getClubDescription());
            club.setClubForm(clubRequestDto.getClubForm());
            club.setClubApproved(clubRequestDto.getClubApproved());
            club.setUpdatedAt(LocalDateTime.now());

            clubDAO.updateClub(id, club);
            BaseResponseDto baseResponseDto = new BaseResponseDto();
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("클럽 생성 완료");
            return baseResponseDto;
        }
    }

    @Override
    public BaseResponseDto updateMember(Long findUserId, Long updateUserId, Long clubId) {
        List<UserClub> userClubList = userClubRepository.findAll();
        User updateUser = userRepository.getById(updateUserId);
        Club club = clubRepository.getById(clubId);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

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
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("클럽 멤버 업데이트 완료");
        } else {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽 멤버 업데이트 실패");
        }

        return baseResponseDto;
    }

    @Override
    public BaseResponseDto removeMember(Long findUserId, Long clubId) {
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
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        baseResponseDto.setSuccess(true);
        baseResponseDto.setMsg("멤버 삭제 완료");

        return baseResponseDto;
    }

    @Override
    public BaseResponseDto addMember(Long userId, Long clubId) {
        User user = userRepository.getById(userId);
        if (user.getId()==null){
            BaseResponseDto baseResponseDto = new BaseResponseDto();
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("해당 유저 없음");
            return baseResponseDto;
        }else{
            Club club = clubRepository.getById(clubId);

            UserClub userClub = new UserClub();
            userClub.setClub(club);
            userClub.setUser(user);
            userClubRepository.save(userClub);

            BaseResponseDto baseResponseDto = new BaseResponseDto();
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("멤버 추가");
            return baseResponseDto;
        }

    }

    @Override
    public List<ClubListResponseDto> findAllClubs() {
        return clubRepository.findAll().stream()
                .map(club -> new ClubListResponseDto(club.getClubName(),club.getLogoUrl()))
                .collect(Collectors.toList());
    }
}
