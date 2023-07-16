package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    @Autowired
    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public Object getProjectById(Long id) {
        if(projectDAO.selectProjectById(id).isEmpty()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("프로젝트가 존재하지 않음");

            return resultResponseDto;
        } else {
            Project selectedProject = projectDAO.selectProjectById(id).get();
            ProjectResponseDto projectResponseDto = new ProjectResponseDto();
            projectResponseDto.setId(selectedProject.getId());
            projectResponseDto.setProjectName(selectedProject.getProjectName());
            projectResponseDto.setProjectDescription(selectedProject.getProjectDescription());
            projectResponseDto.setProjectDetail(selectedProject.getProjectDetail());
            projectResponseDto.setImageUrl(selectedProject.getImageUrl());
            projectResponseDto.setProjectStatus(selectedProject.getProjectStatus());
            projectResponseDto.setProjectPeriod(selectedProject.getProjectPeriod());
            projectResponseDto.setProjectTechStacks(selectedProject.getProjectTechStacks());
            projectResponseDto.setProjectCategory(selectedProject.getProjectCategory());
            projectResponseDto.setCreatedAt(String.valueOf(selectedProject.getCreatedAt()));
            projectResponseDto.setUpdatedAt(String.valueOf(selectedProject.getUpdatedAt()));
            projectResponseDto.setUploadedUserId(selectedProject.getUser().getId().intValue());
            if (selectedProject.getClub() != null) {
                projectResponseDto.setBelongedClubId(selectedProject.getClub().getId().intValue());
                projectResponseDto.setBelongedClubName(selectedProject.getClub().getClubName());
            } else {
                projectResponseDto.setBelongedClubId(null);
                projectResponseDto.setBelongedClubName(null);
            }
            projectResponseDto.setClub(selectedProject.getClub());

            return projectResponseDto;
        }
    }

    @Override
    public ResultResponseDto removeProject(Long id) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if(projectDAO.selectProjectById(id).isEmpty()) {
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("프로젝트가 존재하지 않음");
        } else {
            projectDAO.removeProject(id);
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("프로젝트 삭제 됨");
        }

        return resultResponseDto;
    }

    @Override
    public ResultResponseDto createProject(ProjectRequestDto projectRequestDto) {
        Project project = new Project();
        project.setProjectName(projectRequestDto.getProjectName());
        project.setProjectDescription(projectRequestDto.getProjectDescription());
        project.setImageUrl(projectRequestDto.getImageUrl());
        project.setProjectDescription(projectRequestDto.getProjectDescription());
        project.setProjectDetail(projectRequestDto.getProjectDetail());
        project.setProjectStatus(projectRequestDto.getProjectStatus());
        project.setProjectPeriod(projectRequestDto.getProjectPeriod());
        project.setProjectTechStacks(project.getProjectTechStacks());
        project.setProjectCategory(projectRequestDto.getProjectCategory());
//        project.setUser(null);
//        project.setClub(null);

        Project createdProject = projectDAO.createProject(project);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("프로젝트 생성 완료");

        return resultResponseDto;
    }
}
