package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.UserResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Object getUserById(Long id) {
        if(userDAO.selectUserById(id).isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("유저가 존재하지 않음");

            return resultResponseDto;
        }else{
            User selectedUser = userDAO.selectUserById(id).get();
            UserResponseDto userResponseDto = new UserResponseDto();
            userResponseDto.setId(selectedUser.getId());
            userResponseDto.setUserName(selectedUser.getUsername());
            userResponseDto.setEmail(selectedUser.getEmail());
            userResponseDto.setCreatedAt(String.valueOf(selectedUser.getCreatedAt()));
            userResponseDto.setUpdatedAt(String.valueOf(selectedUser.getUpdatedAt()));
            if(userDAO.getClubByUser(id)!=null){
                List<Club> clubList = userDAO.getClubByUser(id);
                List<String> ans = new ArrayList<>();
                for (Club clubs : clubList){
                    ans.add(clubs.getClubName());
                }
                userResponseDto.setClubs(ans);
            }else{
                userResponseDto.setClubs(null);
            }
            return userResponseDto;
        }
    }

    @Override
    public ResultResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws Exception {

        Optional<User> selectedUser = userDAO.selectUserById(id);

        if(selectedUser.isEmpty()){
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("유저가 존재하지 않습니다.");
            return resultResponseDto;
        }

        User user = selectedUser.get();
        user.setUserName(userRequestDto.getUserName());
        user.setEmail(userRequestDto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());

        userDAO.updateUser(id, user);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("유저 업데이트 완료");
        return resultResponseDto;
    }
}
