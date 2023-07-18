package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectTechStackDAO;
import org.knulikelion.challengers_backend.data.dto.response.ProjectTechStackResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectTechStack;
import org.knulikelion.challengers_backend.data.repository.ProjectTechStackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectTechStackDAOImpl implements ProjectTechStackDAO {
    private final ProjectTechStackRepository projectTechStackRepository;

    @Autowired
    public ProjectTechStackDAOImpl(ProjectTechStackRepository projectTechStackRepository) {
        this.projectTechStackRepository = projectTechStackRepository;
    }

    @Override
    public List<ProjectTechStackResponseDto> getTechStack(Long id) {
        return projectTechStackRepository
                .findAllByProjectId(id)
                .stream()
                .map(ProjectTechStackResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectTechStack createTechStack(ProjectTechStack projectTechStack) {
        return projectTechStackRepository.save(projectTechStack);
    }

    @Override
    public void removeTechStack(Long projectId) {
        List<ProjectTechStack> selectedTechStack = projectTechStackRepository.findAllByProjectId(projectId);

        for (ProjectTechStack techStack : selectedTechStack) {
            techStack.setProject(null);
            projectTechStackRepository.delete(techStack);
        }
    }
}
