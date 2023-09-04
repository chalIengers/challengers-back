package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {
    List<EmailVerification> findByEmail(String email);
}
