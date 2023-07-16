package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface ProjectService {
    Object getProjectById(Long id);
    ResultResponseDto removeProject(Long id);
    ResultResponseDto createProject(ProjectRequestDto projectRequestDto);
}
