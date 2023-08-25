package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.entity.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDAO {
    Optional<Project> selectProjectById(Long id);
    List<Project> getAllProjects(int page, int size);
    Project createProject(Project project);
    Project updateProject(Project project);
    void removeProject(Long id);
}
