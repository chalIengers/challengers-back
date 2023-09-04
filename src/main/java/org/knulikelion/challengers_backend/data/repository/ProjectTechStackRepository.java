package org.knulikelion.challengers_backend.data.repository;

import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectTechStackRepository extends JpaRepository<ProjectTechStack, Long> {
    List<ProjectTechStack> findAllByProjectId(Long projectId);
    List<ProjectTechStack> findAll();
}
