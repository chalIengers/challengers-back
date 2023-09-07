package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectTechStacksResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface ProjectService {
    Object getProjectById(Long id);
    Page<AllProjectResponseDto> getAllProject(int page, int size, String categories, String sort, List<String> techStacks);
    BaseResponseDto removeProject(Long id);
    BaseResponseDto createProject(ProjectRequestDto projectRequestDto, String token);
    BaseResponseDto updateProject(Long id, ProjectRequestDto projectRequestDto, String token);
    Page<AllProjectResponseDto> getProjectsInMonth(YearMonth yearMonth, Pageable pageable);
    List<AllProjectTechStacksResponseDto> getProjectTechStacks();
}
