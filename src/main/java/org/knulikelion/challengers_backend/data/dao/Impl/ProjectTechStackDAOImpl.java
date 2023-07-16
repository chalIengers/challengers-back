package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectTechStackDAO;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;
import org.knulikelion.challengers_backend.data.repository.ProjectTechStackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectTechStackDAOImpl implements ProjectTechStackDAO {
    private final ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    public ProjectTechStackDAOImpl(ProjectTechStackRepository projectTechStackRepository) {
        this.projectTechStackRepository = projectTechStackRepository;
    }

    @Override
    public ProjectTechStack createTechStack(ProjectTechStack projectTechStack) {
        return projectTechStackRepository.save(projectTechStack);
    }
}
