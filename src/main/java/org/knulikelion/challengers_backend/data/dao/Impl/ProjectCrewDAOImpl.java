package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectCrewDAOImpl implements ProjectCrewDAO {
    private final ProjectCrewRepository projectCrewRepository;

    @Autowired
    public ProjectCrewDAOImpl(ProjectCrewRepository projectCrewRepository) {
        this.projectCrewRepository = projectCrewRepository;
    }

    @Override
    public ProjectCrew createCrew(ProjectCrew projectCrew) {
        return projectCrewRepository.save(projectCrew);
    }

    @Override
    public List<ProjectCrewResponseDto> getCrew(Long id) {
        return projectCrewRepository
                .findAllByProjectId(id)
                .stream()
                .map(ProjectCrewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCrew(Long projectId) {
        List<ProjectCrew> selectedCrew = projectCrewRepository.findAllByProjectId(projectId);

        for (ProjectCrew projectCrew : selectedCrew) {
            projectCrew.setProject(null);
            projectCrewRepository.delete(projectCrew);
        }
    }

    @Override
    public Optional<ProjectCrew> selectById(Long id) {
        return projectCrewRepository.findById(id);
    }

    @Override
    public ProjectCrew updateCrew(ProjectCrew projectCrew) {
        Optional<ProjectCrew> updateCrew  = projectCrewRepository.findById(projectCrew.getId());
        ProjectCrew updatedProjectCrew;

        if(updateCrew.isEmpty()) {
            updatedProjectCrew = projectCrewRepository.save(projectCrew);
            return updatedProjectCrew;
        }
        return null;
    }
}