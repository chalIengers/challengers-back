package org.knulikelion.challengers_backend.data.dao.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.entity.MonthlyViews;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.repository.MonthlyViewsRepository;
import org.knulikelion.challengers_backend.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.Optional;

@Component
public class ProjectDAOImpl implements ProjectDAO {
    private final ProjectRepository projectRepository;
    private final MonthlyViewsRepository monthlyViewsRepository;

    @Autowired
    public ProjectDAOImpl(ProjectRepository projectRepository, MonthlyViewsRepository monthlyViewsRepository) {
        this.projectRepository = projectRepository;
        this.monthlyViewsRepository = monthlyViewsRepository;
    }

    @Override
    public Optional<Project> selectProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }

    @Override
    public void removeProject(Long id) {
        Project selectedProject = projectRepository.findById(id).get();
        selectedProject.setClub(null);
        selectedProject.setUser(null);
        projectRepository.delete(selectedProject);
    }

    @Override
    public void incrementViewCount(Long id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if(optionalProject.isPresent()) {
            Project project = optionalProject.get();

            YearMonth currentYearMonth = YearMonth.now();
            MonthlyViews monthlyViews = monthlyViewsRepository.findByProjectAndMonth(project,currentYearMonth).orElseGet(() -> {
                MonthlyViews newMonthlyviews = new MonthlyViews();
                newMonthlyviews.setProject(project);
                newMonthlyviews.setMonth(currentYearMonth);
                newMonthlyviews.setViewCount(0);
                return monthlyViewsRepository.save(newMonthlyviews);
            });
            monthlyViews.setViewCount(monthlyViews.getViewCount() + 1);

            monthlyViewsRepository.save(monthlyViews);
        }
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        Optional<Project> selectedProject = projectRepository.findById(project.getId());
        Project updatedProject;

        if(selectedProject.isPresent()) {
            updatedProject = projectRepository.save(project);
            return updatedProject;
        }

        return null;
    }
}
