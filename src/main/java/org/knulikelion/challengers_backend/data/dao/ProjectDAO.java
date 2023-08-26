package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectDAO {
    Optional<Project> selectProjectById(Long id);
    Page<Project> getAllProjects(Pageable pageable);
    Project createProject(Project project);
    Project updateProject(Project project);
    void removeProject(Long id);
}
