package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectLinkDAO;
import org.knulikelion.challengers_backend.data.dto.response.ProjectLinkResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.entity.ProjectLink;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;
import org.knulikelion.challengers_backend.data.repository.ProjectLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectLinkDAOImpl implements ProjectLinkDAO {
    private final ProjectLinkRepository projectLinkRepository;

    @Autowired
    public ProjectLinkDAOImpl(ProjectLinkRepository projectLinkRepository) {
        this.projectLinkRepository = projectLinkRepository;
    }

    @Override
    public ProjectLink createLink(ProjectLink projectLink) {
        return projectLinkRepository.save(projectLink);
    }

    @Override
    public List<ProjectLinkResponseDto> getLink(Long id) {
        return projectLinkRepository
                .findAllByProjectId(id)
                .stream()
                .map(ProjectLinkResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public void removeLink(Long projectId) {
        List<ProjectLink> selectedLink = projectLinkRepository.findAllByProjectId(projectId);

        for (ProjectLink projectLink : selectedLink) {
            projectLink.setProject(null);
            projectLinkRepository.delete(projectLink);
        }
    }
}
