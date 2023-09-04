package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.AdminNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminNoticeRepository extends JpaRepository<AdminNotice, Long> {
}
