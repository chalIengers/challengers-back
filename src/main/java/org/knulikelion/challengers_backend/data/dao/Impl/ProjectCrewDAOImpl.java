package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public List<ProjectCrewResponseDto> getCrew(Long crewId) {
        return projectCrewRepository
                .findAllByProjectId(crewId)
                .stream()
                .map(ProjectCrewResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ProjectCrewResponseDto>> getCrews(Long projectId) {
       List<ProjectCrew> projectCrews = selectByProjectId(projectId);

        if(projectCrews.isEmpty()) {
            return Collections.emptyMap();
        }else {
            List<ProjectCrewResponseDto> crewsInproject = projectCrews.stream()
                    .map(ProjectCrewResponseDto::new)
                    .collect(Collectors.toList());

            Map<String,List<ProjectCrewResponseDto>> crewsGroupedByPosition
                    = crewsInproject.stream().collect(Collectors.groupingBy(ProjectCrewResponseDto::getPosition));

            return crewsGroupedByPosition;
        }
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
    public Optional<ProjectCrew> selectById(Long crewId) {
        return projectCrewRepository.findById(crewId);
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

    @Override
    public List<ProjectCrew> selectByProjectId(Long projectId) {
        return projectCrewRepository.findAllByProjectId(projectId);
    }
}