package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectCrewServiceImpl implements ProjectCrewService {

    final private ProjectCrewRepository projectCrewRepository;

    public ProjectCrewServiceImpl(ProjectCrewRepository projectCrewRepository) {
        this.projectCrewRepository = projectCrewRepository;
    }

    @Override
    public ProjectCrewRequestDto createCrew(ProjectCrewRequestDto projectCrewRequestDto) {
        ProjectCrew projectCrew = ProjectCrew.builder()
                .projectCrewName(projectCrewRequestDto.getName())
                .projectCrewPosition(projectCrewRequestDto.getPosition())
                .projectCrewRole(projectCrewRequestDto.getRole())
                .build();

        ProjectCrew saveCrew = projectCrewRepository.save(projectCrew);

        ProjectCrewRequestDto saveCrewDto = ProjectCrewRequestDto.builder()
                .id(saveCrew.getId())
                .name(saveCrew.getProjectCrewName())
                .position(saveCrew.getProjectCrewPosition())
                .role(saveCrew.getProjectCrewRole())
                .build();
        return saveCrewDto;
    }

    @Override
    public ProjectCrewRequestDto readCrew(Long id) {
        Optional<ProjectCrew> projectCrew = projectCrewRepository.findById(id);

        ProjectCrewRequestDto readCrewDto = ProjectCrewRequestDto.builder()
                .id(projectCrew.get().getId())
                .name(projectCrew.get().getProjectCrewName())
                .role(projectCrew.get().getProjectCrewRole())
                .position(projectCrew.get().getProjectCrewPosition())
                .build();

        return readCrewDto;
    }

    @Override
    public void updateCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto) throws Exception {

        ProjectCrew projectCrew = ProjectCrew.builder()
                .id(id)
                .projectCrewName(projectCrewRequestDto.getName())
                .projectCrewRole(projectCrewRequestDto.getRole())
                .projectCrewPosition(projectCrewRequestDto.getPosition())
                .build();
        projectCrewRepository.save(projectCrew);
    }

    @Override
    public void deleteCrew(Long id) throws Exception {
        projectCrewRepository.deleteById(id);
    }
}
