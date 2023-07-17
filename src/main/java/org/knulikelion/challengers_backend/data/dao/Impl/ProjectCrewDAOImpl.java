package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProjectCrewDAOImpl implements ProjectCrewDAO {
    private final ProjectCrewRepository projectCrewRepository;

    @Autowired
    public ProjectCrewDAOImpl(ProjectCrewRepository projectCrewRepository) {
        this.projectCrewRepository = projectCrewRepository;
    }

    @Override
    public Optional<ProjectCrew> selectById(Long id) {
        return projectCrewRepository.findById(id);
    }

    @Override
    public ProjectCrew createCrew(ProjectCrew projectCrew) {
        return projectCrewRepository.save(projectCrew);
    }

    @Override
    public void removeCrew(Long id) {
        projectCrewRepository.deleteById(id);
    }
}

