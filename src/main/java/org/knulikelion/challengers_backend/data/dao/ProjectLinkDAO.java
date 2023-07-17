package org.knulikelion.challengers_backend.data.dao;

import org.knulikelion.challengers_backend.data.dto.response.ProjectLinkResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectLink;

import java.util.List;

public interface ProjectLinkDAO {
    ProjectLink createLink(ProjectLink projectLink);
    List<ProjectLinkResponseDto> getLink(Long id);
    void removeLink(Long projectId);
}
