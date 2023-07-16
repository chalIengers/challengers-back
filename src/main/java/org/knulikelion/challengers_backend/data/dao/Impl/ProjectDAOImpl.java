package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectDAOImpl implements ProjectDAO {
    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectDAOImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public Optional<Project> selectProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public void removeProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
}
