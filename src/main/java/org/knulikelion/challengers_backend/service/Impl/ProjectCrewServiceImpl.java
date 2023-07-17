package org.knulikelion.challengers_backend.service.Impl;

import lombok.RequiredArgsConstructor;
import org.knulikelion.challengers_backend.data.dto.ProjectCrewDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class ProjectCrewServiceImpl implements ProjectCrewService {

    final private ProjectCrewRepository projectCrewRepository;

    public ProjectCrewServiceImpl(ProjectCrewRepository projectCrewRepository) {
        this.projectCrewRepository = projectCrewRepository;
    }

    @Override
    public ProjectCrewDto.Info createCrew(ProjectCrewDto.ProjectCrewRequestDto projectCrewRequestDto) {
        ProjectCrew projectCrew = ProjectCrew.builder()
                .projectCrewName(projectCrewRequestDto.getName())
                .projectCrewPosition(projectCrewRequestDto.getPosition())
                .projectCrewRole(projectCrewRequestDto.getRole())
                .build();

        ProjectCrew saveCrew = projectCrewRepository.save(projectCrew);

        ProjectCrewDto.Info saveCrewDto = ProjectCrewDto.Info.builder()
                .id(saveCrew.getId())
                .name(saveCrew.getProjectCrewName())
                .position(saveCrew.getProjectCrewPosition())
                .role(saveCrew.getProjectCrewRole())
                .build();
        return saveCrewDto;
    }

    @Override
    public ProjectCrewDto.Info readCrew(Long id) {
        Optional<ProjectCrew> projectCrew = projectCrewRepository.findById(id);

        ProjectCrewDto.Info readCrewDto = ProjectCrewDto.Info.builder()
                .id(projectCrew.get().getId())
                .name(projectCrew.get().getProjectCrewName())
                .role(projectCrew.get().getProjectCrewRole())
                .position(projectCrew.get().getProjectCrewPosition())
                .build();

        return readCrewDto;
    }

    @Override
    public void updateCrew(Long id, ProjectCrewDto.ProjectCrewRequestDto projectCrewRequestDto) throws Exception {

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
