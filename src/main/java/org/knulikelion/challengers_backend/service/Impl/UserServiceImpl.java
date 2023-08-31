package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.UserResponseDto;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public BaseResponseDto remove1User(String userEmail, String password) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();
        User findUser = userRepository.findByEmail(userEmail);
        if(findUser == null){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("유저를 찾을 수 없습니다.");
            return baseResponseDto;
        }if(passwordEncoder.matches(password, findUser.getPassword())){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("비밀 번호가 일치하지 않습니다.");
            return baseResponseDto;
        } else {
            findUser.setUseAble(false);
            User user = userRepository.save(findUser);
            log.info("[removeUser] 회원 탈퇴 완료 : {}", user);
        }
        return baseResponseDto;
    }

    @Override
    public BaseResponseDto remove2User(String userEmail, String password) {
        return null;
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
