package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ProjectCrewService {

    //팀원 생성
    ResultResponseDto createProjectCrew(ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 조회.
    Object getProjectCrewById(Long id);

    // 팀원 수정
    ResultResponseDto updateProjcetCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 제거
    ResultResponseDto removeProjectCrew(Long id);


}
