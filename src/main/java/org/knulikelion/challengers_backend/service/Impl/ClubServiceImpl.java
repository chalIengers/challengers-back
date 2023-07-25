package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ClubDAO;
import org.knulikelion.challengers_backend.data.dto.request.ClubCreateRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ClubRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ClubResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
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

    private final UserRepository userRepository;


    public ClubServiceImpl(ClubDAO clubDAO, UserRepository userRepository) {
        this.clubDAO = clubDAO;
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
    public ResultResponseDto createClub(ClubCreateRequestDto clubCreateRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        Club club = new Club();
        club.setClubName(clubCreateRequestDto.getClubName());
        club.setLogoUrl(clubCreateRequestDto.getLogoUrl());
        club.setClubDescription(clubCreateRequestDto.getClubDescription());
        club.setClubForm(clubCreateRequestDto.getClubForm());
        club.setClubApproved(0);
        club.setCreatedAt(currentTime);
        club.setUpdatedAt(currentTime);
//        if(clubCreateRequestDto.getMembers().isEmpty()){
//            club.setUsers(null);
//        }else{
//            List<User> users = new ArrayList<>();
//            for(UserRequestDto userId : clubCreateRequestDto.getMembers()){
//                User user = userRepository.getById(userId.getId());
//                users.add(user);
//                users.sort(Comparator.comparing(User::getId));
//                club.setUsers(users);
//            }
//
//        }
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
        }else {

            Club club = clubOptional.get();
            club.setClubName(clubRequestDto.getClubName());
            club.setLogoUrl(clubRequestDto.getLogoUrl());
            club.setClubDescription(clubRequestDto.getClubDescription());
            club.setClubForm(clubRequestDto.getClubForm());
            club.setClubApproved(clubRequestDto.getClubApproved());
            club.setUpdatedAt(currentTime);


//            if(clubRequestDto.getMembers() == null){
//                logger.info("변경 멤버 없음.");
//                club.setUsers(club.getUsers());
//            }else{
//                List<UserRequestDto> selectedUserId = clubRequestDto.getMembers();
//                // 변경 할 멤버 request
//                List<User> wntUser = new ArrayList<>(); //wntUser : 변경 할 User List
//                    for (UserRequestDto wnt : selectedUserId) {
//                        wntUser.add(userRepository.getById(wnt.getId()));
//                    }
//                List<UserRequestDto> selectedUpdateUserId = clubRequestDto.getUpdateMembers();
//                List<User> udpUsers = new ArrayList<>(); //udpUsers : Update 할 User List
//                for (UserRequestDto udp : selectedUpdateUserId) {
//                    udpUsers.add(userRepository.getById(udp.getId()));
//                }
//                if(udpUsers.isEmpty()){
//                    List<User> clubMembers = club.getUsers();
//                    List<User> newList = new ArrayList<>();
//                    for (User findUser : wntUser) {
//                        for (User selectedUser : clubMembers) {
//                            if (!findUser.getId().equals(selectedUser.getId())) {
//                                newList.add(selectedUser);
//                            }
//                        }
//                    }
//                    club.setUsers(newList);
//                }else{
//                    List<User> clubMembers = club.getUsers();
//                    List<User> newList = new ArrayList<>();
//                    for (User findUser : wntUser) {
//                        for (User selectedUser : clubMembers) {
//                            if(!findUser.getId().equals(selectedUser.getId())){
//                                newList.add(selectedUser);
//                            }
//                        }
//                    }
//                    newList.addAll(udpUsers);
//                    club.setUsers(newList);
//                }
//            }
            clubDAO.updateClub(id, club);
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("클럽 생성 완료");
            return resultResponseDto;
        }

    }
}
