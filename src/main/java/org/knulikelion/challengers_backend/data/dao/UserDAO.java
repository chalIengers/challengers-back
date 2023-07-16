package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.User;

import java.util.Optional;

public interface UserDAO {
    Optional<User> selectUserById(Long id);
    void removeUser(Long id);
}
