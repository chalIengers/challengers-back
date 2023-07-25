package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserUpdateRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.UserResponseDto;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public UserServiceImpl(UserDAO userDAO, UserRepository userRepository, ClubRepository clubRepository) {
        this.userDAO = userDAO;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;

    }

    @Override
    public ResultResponseDto createUser(UserRequestDto userRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        User userInfo = new User();
        userInfo.setUserName(userRequestDto.getUserName());
        userInfo.setEmail(userRequestDto.getEmail());
        if (userRequestDto.getClubId() != null){
            userInfo.setClub(clubRepository.getById(userRequestDto.getClubId()));
        }else{
            userInfo.setClub(null);
        }
        userInfo.setCreatedAt(currentTime);
        userInfo.setUpdatedAt(currentTime);
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("유저 생성 완료");
        return resultResponseDto;
    }

    @Override
    public Object getUserById(Long id) {
        logger.info("");
        if(userDAO.selectUserById(id).isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("유저가 존재하지 않음");

            return resultResponseDto;
        }else{
            User selectedUser = userDAO.selectUserById(id).get();
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setId(selectedUser.getId());
            userResponseDto.setUserName(selectedUser.getUserName());
            userResponseDto.setEmail(selectedUser.getEmail());
            userResponseDto.setCreatedAt(String.valueOf(selectedUser.getCreatedAt()));
            userResponseDto.setUpdatedAt(String.valueOf(selectedUser.getUpdatedAt()));
            if(userDAO.getClubByUser(id)!=null){
                userResponseDto.setClubs(userDAO.getClubByUser(id));
            }else{
                userResponseDto.setClubs(null);
            }
            return userResponseDto;
        }
    }

    @Override
    public ResultResponseDto removeUser(Long id) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if(userDAO.selectUserById(id).isEmpty()){
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("유저가 존재하지 않음");
        }else{
            userDAO.removeUser(id);
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("유저 삭제");
        }
        return resultResponseDto;
    }


    @Override
    public ResultResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) throws Exception {
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<User> selectedUser = userDAO.selectUserById(id);
        if(selectedUser.isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("유저가 존재하지 않습니다.");
            return resultResponseDto;
        }

        User user = selectedUser.get();
        user.setUserName(userUpdateRequestDto.getUserName());
        user.setEmail(userUpdateRequestDto.getEmail());
        user.setUpdatedAt(currentTime);

        List<User> selectedUserById = userRepository.findAllById(Collections.singleton(id));
        for(User findUser : selectedUserById){
            if(findUser.getClub().getId().equals(userUpdateRequestDto.getSelectClubId())){
                findUser.setClub(clubRepository.getById(userUpdateRequestDto.getUpdateClubId()));
            }
        }

        userDAO.updateUser(id, user);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("유저 업데이트 완료");
        return resultResponseDto;
    }
}
