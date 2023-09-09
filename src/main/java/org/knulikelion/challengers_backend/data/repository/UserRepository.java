package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


public interface UserRepository extends JpaRepository<User, Long> {
    User getByEmail(String email);
    User findByEmail(String email);
    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Page<User> findByUseAbleTrue(Pageable pageable);
    Page<User> findByUseAbleFalse(Pageable pageable);
}
