package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ProjectCrewService {

    //팀원 생성
    BaseResponseDto createProjectCrew(ProjectCrewRequestDto projectCrewRequestDto);

    //포지션별 프로젝트 팀원 조회.
    Object getCrewsGroupedByPosition(Long crewId);

    // 팀원 조회.
    Object getProjectCrewById(Long crewId);

    // 팀원 수정
    BaseResponseDto updateProjectCrew(Long crewId, ProjectCrewRequestDto projectCrewRequestDto);

    // 팀원 제거
    BaseResponseDto removeProjectCrew(Long crewId);


}
