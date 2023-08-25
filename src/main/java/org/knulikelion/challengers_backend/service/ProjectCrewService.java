package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ProjectCrewService {

    //팀원 생성
    BaseResponseDto createProjectCrew(ProjectCrewRequestDto projectCrewRequestDto);

    Object getCrewsGroupedByPosition(Long id);

    // 팀원 조회.
    Object getProjectCrewById(Long id);

    // 팀원 수정
    BaseResponseDto updateProjectCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 제거
    BaseResponseDto removeProjectCrew(Long id);


}
