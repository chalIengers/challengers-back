package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ClubAudit;
import org.knulikelion.challengers_backend.data.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ClubAuditRepository extends JpaRepository<ClubAudit, Long> {

    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
    List<ClubAudit> findTop5ByEventTypeOrderByCreatedAtDesc(EventType eventType);
}
