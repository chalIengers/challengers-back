package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectLinkResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;

import java.util.List;
import java.util.Optional;

public interface ProjectCrewDAO {
    ProjectCrew createCrew(ProjectCrew projectCrew);
    List<ProjectCrewResponseDto> getCrew(Long id);
    void removeCrew(Long projectId);
    Optional<ProjectCrew> selectById(Long id);
    ProjectCrew updateCrew(ProjectCrew projectCrew);
}
