package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectCrewDAO {
    ProjectCrew createCrew(ProjectCrew projectCrew);
    List<ProjectCrewResponseDto> getCrew(Long crewId);
    Map<String,List<ProjectCrewResponseDto>> getCrews(Long projectId);
    void removeCrew(Long crewId);
    Optional<ProjectCrew> selectById(Long crewId);
    ProjectCrew updateCrew(ProjectCrew projectCrew);
    List<ProjectCrew> selectByProjectId(Long projectId);
}
