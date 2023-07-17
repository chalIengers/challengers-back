package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ProjectLink;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectLinkRepository extends JpaRepository<ProjectLink, Long> {
    List<ProjectLink> findAllByProjectId(Long projectId);
}
