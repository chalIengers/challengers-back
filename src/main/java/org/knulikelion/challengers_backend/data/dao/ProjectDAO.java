package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.Project;

import java.util.Optional;

public interface ProjectDAO {
    Optional<Project> selectProjectById(Long id);
    Project createProject(Project project);
    void removeProject(Long id);
}
