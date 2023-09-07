package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.Exception.ClubNotFoundException;
import org.knulikelion.challengers_backend.service.Exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ClubResponseDto getClubDetailById(Long id) {
        if (clubDAO.selectClubById(id).isEmpty()){
            throw new RuntimeException("클럽이 존재하지 않음");

        } else{
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
            throw new ClubNotFoundException("해당 클럽이 존재하지 않습니다.");
        }else{
//            클럽 매니저 삭제
            Club club = clubDAO.selectClubById(id).get();
            club.setClubManager(null);

//            UserClub 매핑 값 삭제
            List<UserClub> selectedMapping = userClubRepository.findAllByClubId(id);
            if(!selectedMapping.isEmpty()) {
                for (UserClub userClub : selectedMapping) {
                    userClub.setUser(null);
                    userClub.setClub(null);
                    userClubRepository.delete(userClub);
                }
            }
//            클럽 삭제
            clubRepository.deleteById(id);
        }
        baseResponseDto.setSuccess(true);
        baseResponseDto.setMsg("클럽이 정상적으로 삭제 되었습니다.");
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto createClub(String userEmail, ClubCreateRequestDto clubCreateRequestDto) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.");
        }

        Club club = new Club();
        club.setClubName(clubCreateRequestDto.getClubName());
        club.setLogoUrl(clubCreateRequestDto.getLogoUrl());
        club.setClubDescription(clubCreateRequestDto.getClubDescription());
        club.setClubForm(clubCreateRequestDto.getClubForm());
        club.setClubApproved(0);
        club.setClubManager(user);
        club.setCreatedAt(LocalDateTime.now());
        club.setUpdatedAt(LocalDateTime.now());

        Club createdClub = clubDAO.createClub(club);

        // 클럽 생성자 멤버 추가
        addMember(user.getId(), createdClub.getId());

        return BaseResponseDto.builder()
                .success(true)
                .msg("클럽 생성이 완료되었습니다.")
                .build();
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
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다.");
        }else {
            Optional<Club> club = clubRepository.findById(clubId);

            if (club.isEmpty()) {
                throw new ClubNotFoundException("해당 클럽을 찾을 수 없습니다.");
            } else {

                UserClub userClub = new UserClub();
                userClub.setClub(club.get());
                userClub.setUser(user);
                userClubRepository.save(userClub);

                baseResponseDto.setSuccess(true);
                baseResponseDto.setMsg("성공적으로 클럽 멤버를 추가하였습니다.");
            }
        }
        return baseResponseDto;
    }

    @Override
    public List<UserClubResponseDto> getUsersClub(String email) {
        User user = userRepository.getByEmail(email);
        List<UserClub> userClub = userClubRepository.findByUserId(user.getId());
        List<UserClubResponseDto> userClubResponseDtoList = new ArrayList<>();

        if(userClub != null) {
            for(UserClub temp : userClub) {
                UserClubResponseDto userClubResponseDto = new UserClubResponseDto();
                userClubResponseDto.setId(temp.getClub().getId());
                userClubResponseDto.setName(temp.getClub().getClubName());
                userClubResponseDto.setLogo(temp.getClub().getLogoUrl());

                if(temp.getClub().getClubManager().getId() == user.getId()) {
                    userClubResponseDto.setManager(true);
                    userClubResponseDto.setManagerEmail(null);
                } else {
                    userClubResponseDto.setManager(false);
                    userClubResponseDto.setManagerEmail(temp.getClub().getClubManager().getEmail());
                }

                userClubResponseDtoList.add(userClubResponseDto);
            }

            return userClubResponseDtoList;
        }

        return null;
    }

    @Override
    public Page<ClubListResponseDto> findAllClubs(int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return clubRepository.findAll(pageable).map(club -> new ClubListResponseDto(club.getId(),club.getClubName(),club.getLogoUrl()));
    }

    @Override
    public List<ClubMemberResponseDto> getMembersByClubId(Long clubId) {
        List<UserClub> userClubs = userClubRepository.findByClubId(clubId);
        return userClubs.stream()
                .map(userClub -> new ClubMemberResponseDto(userClub.getUser().getUserName(),userClub.getUser().getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<BaseResponseDto> verifyCreateClub(String userEmail, ClubCreateRequestDto clubCreateRequestDto) {
        User user = userRepository.findByEmail(userEmail);
        if(user == null){
            throw new UserNotFoundException("해당 유저를 찾을 수 없습니다!");
        }

        Club club = clubRepository.findByClubName(clubCreateRequestDto.getClubName());

        if(club != null){
            return new ResponseEntity<>(BaseResponseDto.builder()
                    .success(false)
                    .msg("중복된 클럽 클럽이름입니다!")
                    .build(),
                    HttpStatus.BAD_REQUEST);
        }else{
            return ResponseEntity.ok(BaseResponseDto.builder()
                    .success(true)
                    .msg(user.getUserName()+"님의 "+clubCreateRequestDto.getClubName()+" 클럽은 생성 가능합니다!")
                    .build());
        }
    }
}
