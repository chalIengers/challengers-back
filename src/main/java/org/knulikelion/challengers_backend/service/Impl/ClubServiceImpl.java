package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class ClubServiceImpl implements ClubService {
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
        List<Club> clubList = clubDAO.getAllClubs();

        if (clubList.size() <= 28) {
            for(Club temp : clubList) {
                ClubLogoResponseDto clubLogoResponseDto = new ClubLogoResponseDto();
                if(!temp.getLogoUrl().isEmpty()) {
                    clubLogoResponseDto.setLogoUrl(temp.getLogoUrl());
                    clubLogoResponseDtoList.add(clubLogoResponseDto);
                }
            }
        } else {
            Collections.shuffle(clubList);

            int logoCount = 0;
            for(Club temp : clubList) {
                if(logoCount >= 28) break;

                ClubLogoResponseDto clubLogoResponseDto = new ClubLogoResponseDto();
                if(!temp.getLogoUrl().isEmpty()) {
                    logoCount++;
                    clubLogoResponseDto.setLogoUrl(temp.getLogoUrl());
                    clubLogoResponseDtoList.add(clubLogoResponseDto);
                }
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
    public BaseResponseDto createClub(String userEmail, ClubCreateRequestDto clubCreateRequestDto) {
        User user = userRepository.getByEmail(userEmail);

        Club club = new Club();
        club.setClubName(clubCreateRequestDto.getClubName());
        club.setLogoUrl(clubCreateRequestDto.getLogoUrl());
        club.setClubDescription(clubCreateRequestDto.getClubDescription());
        club.setClubForm(clubCreateRequestDto.getClubForm());
        club.setClubApproved(0);
        club.setClubManager(user);
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
    public BaseResponseDto updateClub(String userEmail, ClubRequestDto clubRequestDto){

        Optional<Club> findClub = clubRepository.findById(clubRequestDto.getClubId());
        User user = userRepository.getByEmail(userEmail);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if(findClub.isEmpty()){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽이 존재하지 않습니다.");
            return baseResponseDto;
        }else {
            Club club = findClub.get();
            if (club.getClubManager().equals(user)) { /*클럽 매니저만 클럽 수정*/
                club.setClubDescription(clubRequestDto.getClubDescription());
                club.setClubForm(clubRequestDto.getClubForm());
                club.setClubName(clubRequestDto.getClubName());
                club.setLogoUrl(clubRequestDto.getLogoUrl());
                club.setUpdatedAt(LocalDateTime.now());

                clubRepository.save(club);

                baseResponseDto.setSuccess(true);
                baseResponseDto.setMsg("클럽 수정을 완료하였습니다.");
            } else {
                baseResponseDto.setSuccess(false);
                baseResponseDto.setMsg("클럽 수정 권한이 없습니다.");
            }
            return baseResponseDto;
        }
    }

    @Override
    public BaseResponseDto removeMember(String userEmail,String deleteUserEmail, Long clubId) {
        User findUser = userRepository.getByEmail(deleteUserEmail);
        Optional<Club> findClub = clubRepository.findById(clubId);
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        if (findClub.isEmpty()) {
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽이 존재하지 않음");
            return baseResponseDto;
        }

        Club club = findClub.get();
        User user = userRepository.getByEmail(userEmail);
        if (club.getClubManager().equals(user)) {
            UserClub deleteEntity = userClubRepository.findByUserIdAndClubId(findUser.getId(), clubId);

            if (deleteEntity == null) {
                baseResponseDto.setSuccess(false);
                baseResponseDto.setMsg("멤버가 존재하지 않음");
                return baseResponseDto;
            } else {
                deleteEntity.setUser(null);
                deleteEntity.setClub(null);
                userClubRepository.delete(deleteEntity);

                baseResponseDto.setSuccess(true);
                baseResponseDto.setMsg("성공적으로 멤버를 삭제했습니다.");
                return baseResponseDto;
            }
        }else{
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("클럽 멤버 수정 권한이 없습니다.");
            return baseResponseDto;
        }
    }


    @Override
    public BaseResponseDto addMember(Long userId, Long clubId) {
        User user = userRepository.getById(userId);
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        if (user.getId()==null){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("해당 유저 없음");
        }else{
            Club club = clubRepository.getById(clubId);

            UserClub userClub = new UserClub();
            userClub.setClub(club);
            userClub.setUser(user);
            userClubRepository.save(userClub);

            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("성공적으로 클럽 멤버를 추가하였습니다.");
        }
        return baseResponseDto;
    }

    @Override
    public ClubListResponseDto getUsersClub(String email) {
        User user = userRepository.getByEmail(email);
        UserClub userClub = userClubRepository.findByUserId(user.getId());
        Club club = clubRepository.getById(userClub.getClub().getId());

        if(club != null) {
            ClubListResponseDto clubListResponseDto = new ClubListResponseDto();
            clubListResponseDto.setId(club.getId());
            clubListResponseDto.setLogo(club.getLogoUrl());
            clubListResponseDto.setName(club.getClubName());

            return clubListResponseDto;
        }

        return null;
    }

    @Override
    public List<ClubListResponseDto> findAllClubs(int page, int size) {
        return clubRepository.findAll(PageRequest.of(page,size)).stream()
                .map(club -> new ClubListResponseDto(club.getId(), club.getClubName(), club.getLogoUrl()))
                .collect(Collectors.toList());
    }
}
