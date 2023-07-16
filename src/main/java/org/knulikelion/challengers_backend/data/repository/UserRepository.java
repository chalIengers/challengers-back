package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
