package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {
    Page<Project> findAll(Pageable pageable);
    List<Project> findAllByUser(User user);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
