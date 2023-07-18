package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.UserDAO;
import org.knulikelion.challengers_backend.data.entity.Club;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDAOImpl implements UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
    private final UserRepository userRepository;
    @Autowired
    public UserDAOImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> selectUserById(Long id) {
        logger.info("selected User id:"+id);
        List<Club> clubs = userRepository.findAllByUserId(id);
        return userRepository.findById(id);
    }

    @Override
    public void removeUser(Long id) {
        logger.info("delete User id:"+id);
        userRepository.deleteById(id);
    }
}
