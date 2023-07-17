package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.ProjectCrewDto;

public interface ProjectCrewService {

    // 팀원 작성.
    ProjectCrewDto.Info createCrew(ProjectCrewDto.ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 목록
    ProjectCrewDto.Info readCrew(Long id);

    void updateCrew(Long id, ProjectCrewDto.ProjectCrewRequestDto projectCrewRequestDto) throws Exception;

    void deleteCrew(Long id) throws Exception;

}
