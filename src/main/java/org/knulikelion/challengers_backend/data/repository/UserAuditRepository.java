package org.knulikelion.challengers_backend.data.repository;


import org.knulikelion.challengers_backend.data.entity.UserAudit;
import org.knulikelion.challengers_backend.data.enums.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserAuditRepository extends JpaRepository<UserAudit,Long> {

    Long countByDeletedAtBetween(LocalDateTime start, LocalDateTime end);
    List<UserAudit> findTop5ByEventTypeOrderByCreatedAtDesc(EventType eventType);

}
