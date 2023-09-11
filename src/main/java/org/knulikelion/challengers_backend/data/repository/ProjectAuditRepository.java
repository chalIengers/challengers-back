package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ProjectAudit;
import org.knulikelion.challengers_backend.data.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectAuditRepository extends JpaRepository<ProjectAudit, Long> {
    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
    List<ProjectAudit> findTop5ByOrderByCreatedAtDesc();
    List<ProjectAudit> findTop5ByEventTypeOrderByCreatedAtDesc(EventType eventType);

}
