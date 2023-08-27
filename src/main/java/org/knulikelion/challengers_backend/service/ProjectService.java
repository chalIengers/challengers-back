package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.springframework.data.domain.Page;

import java.time.YearMonth;
import java.util.List;

public interface ProjectService {
    Object getProjectById(Long id);
    Page<AllProjectResponseDto> getAllProjects(int page, int size);
    BaseResponseDto removeProject(Long id);
    BaseResponseDto createProject(ProjectRequestDto projectRequestDto, String token);
    BaseResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto, String token);

    List<AllProjectResponseDto> getProjectsInMonth(YearMonth yearMonth);
}
