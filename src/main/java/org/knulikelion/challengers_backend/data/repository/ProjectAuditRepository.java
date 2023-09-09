package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ProjectAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ProjectAuditRepository extends JpaRepository<ProjectAudit, Long> {
    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
}
