package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.entity.UserClub;
import org.knulikelion.challengers_backend.data.repository.UserClubRepository;
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
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;
    @Autowired
    public UserDAOImpl(UserClubRepository userClubRepository, UserRepository userRepository) {
        this.userClubRepository = userClubRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectUserById(Long id) {
        logger.info("Selected User Id:"+id);
        return userRepository.findById(id);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
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
    public List<Club> getClubByUser(Long id) {
        List<UserClub> userClubList = userClubRepository.findAll();
        List<Club> clubList = new ArrayList<>();

        for (UserClub userClub : userClubList){
            if(userClub.getUser().getId().equals(id)){
                clubList.add(userClub.getClub());
            }
        }
        return clubList;
    }
}
