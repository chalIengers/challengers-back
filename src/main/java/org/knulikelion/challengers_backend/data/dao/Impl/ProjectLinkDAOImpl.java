package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectLinkDAO;
import org.knulikelion.challengers_backend.data.entity.ProjectLink;
import org.knulikelion.challengers_backend.data.repository.ProjectLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
}
