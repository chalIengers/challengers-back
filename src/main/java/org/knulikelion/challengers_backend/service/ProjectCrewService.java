package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;

public interface ProjectCrewService {

    // 팀원 작성.
    ProjectCrewRequestDto createCrew(ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 목록
    ProjectCrewRequestDto readCrew(Long id);

    void updateCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto) throws Exception;

    void deleteCrew(Long id) throws Exception;

}
