package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ClubAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ClubAuditRepository extends JpaRepository<ClubAudit, Long> {

    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
}
