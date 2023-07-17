package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.dto.response.ProjectTechStackResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;

import java.util.List;

public interface ProjectTechStackDAO {
    List<ProjectTechStackResponseDto> getTechStack(Long id);
    ProjectTechStack createTechStack(ProjectTechStack projectTechStack);
}
