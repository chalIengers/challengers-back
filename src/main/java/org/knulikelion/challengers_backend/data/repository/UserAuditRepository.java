package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserAuditRepository extends JpaRepository<UserAudit,Long> {

    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
}
