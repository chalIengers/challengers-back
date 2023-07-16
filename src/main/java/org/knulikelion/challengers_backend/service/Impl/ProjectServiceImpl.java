package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.Project;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectDAO projectDAO;
    private final ProjectCrewDAO projectCrewDAO;
//    임시
    private final UserRepository userRepository;
    @Autowired
    public ProjectServiceImpl(ProjectDAO projectDAO, ProjectCrewDAO projectCrewDAO, UserRepository userRepository) {
        this.projectDAO = projectDAO;
        this.projectCrewDAO = projectCrewDAO;
        this.userRepository = userRepository;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();

        Project project = new Project();
        project.setProjectName(projectRequestDto.getProjectName());
        project.setImageUrl(projectRequestDto.getImageUrl());
        project.setProjectDescription(projectRequestDto.getProjectDescription());
        project.setProjectDetail(projectRequestDto.getProjectDetail());
        project.setProjectStatus(projectRequestDto.getProjectStatus());
        project.setProjectPeriod(projectRequestDto.getProjectPeriod());
        project.setProjectTechStacks(projectRequestDto.getProjectTechStacks());
        project.setProjectCategory(projectRequestDto.getProjectCategory());
        project.setCreatedAt(currentTime);
        project.setUpdatedAt(currentTime);
        if(projectRequestDto.getBelongedClubId() == 0) {
            project.setClub(null);
        } else {
//            이 부분 추가해야 함 코드
            project.setClub(null);
        }

//        임시
        User user = new User();
        user.setUserName("전윤환");
        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        user.setEmail("yunsol267@gmail.com");

        User savedUser = userRepository.save(user);
        project.setUser(savedUser);

        Project createdProject = projectDAO.createProject(project);

        for (ProjectCrewRequestDto projectCrewRequestDto : projectRequestDto.getProjectCrew()) {
            ProjectCrew projectCrew = new ProjectCrew();
            projectCrew.setProject(createdProject);
            projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
            projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
            projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
            projectCrew.setCreatedAt(currentTime);
            projectCrew.setUpdatedAt(currentTime);
            projectCrewDAO.createCrew(projectCrew);
        }

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("프로젝트 생성 완료");

        return resultResponseDto;
    }
}
