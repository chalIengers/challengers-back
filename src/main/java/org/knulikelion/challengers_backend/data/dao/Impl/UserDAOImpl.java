package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.ClubRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserDAOImpl(ClubRepository clubRepository, UserRepository userRepository) {
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectUserById(Long id) {
        logger.info("Selected User Id:"+id);
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, User user) throws Exception {
        Optional <User> selectedUser = userRepository.findById(id);

        User updatedUser;
        if(selectedUser.isPresent()){
            User userInfo = selectedUser.get();
            userInfo.setUserName(user.getUserName());
            userInfo.setEmail(user.getEmail());

            updatedUser = userRepository.save(userInfo);
        }else{
            throw new Exception();
        }
        return updatedUser;
    }

    @Override
    public void removeUser(Long id) {
        logger.info("Delete User Id:"+id);
        userRepository.deleteById(id);
    }

    @Override
    public List<String> getClubByUser(Long id) {
        User user = userRepository.findById(id).get();
        List<Club> clubs = clubRepository.findAll(); //다 가져와

        List<String> ans = new ArrayList<>();

        for(Club temp : clubs){
            List<User> userList = temp.getUsers();
            if(userList.contains(user))
                ans.add(temp.getClubName());
        }
        return ans;
    }
}
