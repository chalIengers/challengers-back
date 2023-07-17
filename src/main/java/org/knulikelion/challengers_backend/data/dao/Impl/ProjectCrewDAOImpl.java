package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
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
}
