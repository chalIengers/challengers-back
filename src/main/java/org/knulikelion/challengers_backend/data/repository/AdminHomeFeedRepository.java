package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.AdminHomeFeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminHomeFeedRepository extends JpaRepository<AdminHomeFeed, Long> {
    List<AdminHomeFeed> findAllByOrderByCreatedAtDesc();
}
