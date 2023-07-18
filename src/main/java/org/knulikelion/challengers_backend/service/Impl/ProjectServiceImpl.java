package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.*;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectLinkRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ProjectTechStackRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.entity.*;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectDAO projectDAO;
    private final ProjectCrewDAO projectCrewDAO;
    private final ProjectLinkDAO projectLinkDAO;
    private final ProjectTechStackDAO projectTechStackDAO;
    private final ClubDAO clubDAO;
    private final UserDAO userDAO;

    @Autowired
    public ProjectServiceImpl(
            ProjectDAO projectDAO,
            ProjectCrewDAO projectCrewDAO,
            ProjectLinkDAO projectLinkDAO,
            ProjectTechStackDAO projectTechStackDAO,
            ClubDAO clubDAO,
            UserDAO userDAO
    ) {
        this.projectDAO = projectDAO;
        this.projectCrewDAO = projectCrewDAO;
        this.projectLinkDAO = projectLinkDAO;
        this.projectTechStackDAO = projectTechStackDAO;
        this.clubDAO = clubDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Object getProjectById(Long id) {
        if(projectDAO.selectProjectById(id).isEmpty()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("프로젝트가 존재하지 않음");
            logger.info("[Log] 프로젝트가 존재하지 않음, ID:" + id);

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
            projectResponseDto.setProjectCategory(selectedProject.getProjectCategory());
            projectResponseDto.setCreatedAt(String.valueOf(selectedProject.getCreatedAt()));
            projectResponseDto.setUpdatedAt(String.valueOf(selectedProject.getUpdatedAt()));
            projectResponseDto.setUploadedUserId(selectedProject.getUser().getId().intValue());
            if (selectedProject.getClub() != null) {
                projectResponseDto.setBelongedClubId(selectedProject.getClub().getId().intValue());
                projectResponseDto.setBelongedClubName(selectedProject.getClub().getClubName());
            } else {
                logger.info("[Log] 클럽이 존재하지 않음");
                projectResponseDto.setBelongedClubId(null);
                projectResponseDto.setBelongedClubName(null);
            }
            List<ProjectTechStackResponseDto> techStacks = projectTechStackDAO.getTechStack(projectResponseDto.getId());
            projectResponseDto.setProjectTechStack(techStacks);
            List<ProjectLinkResponseDto> projectLinks = projectLinkDAO.getLink(projectResponseDto.getId());
            projectResponseDto.setProjectLink(projectLinks);
            List<ProjectCrewResponseDto> projectCrews = projectCrewDAO.getCrew(projectResponseDto.getId());
            projectResponseDto.setProjectCrew(projectCrews);
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
            logger.info("[Log] 포함된 기술 스텍 전체 삭제");
            projectTechStackDAO.removeTechStack(id);
            logger.info("[Log] 포함된 링크 전체 삭제");
            projectLinkDAO.removeLink(id);
            logger.info("[Log] 포함된 크루 전체 삭제");
            projectCrewDAO.removeCrew(id);
            logger.info("[Log] 포함된 프로젝트 삭제");
            projectDAO.removeProject(id);
            logger.info("[Log] 프로젝트 삭제 완료");
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("프로젝트 삭제 됨");
        }

        return resultResponseDto;
    }

    @Override
    public ResultResponseDto createProject(ProjectRequestDto projectRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        Project project = new Project();
        if(userDAO.selectUserById(projectRequestDto.getUploadedUserId()).isEmpty()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("존재하지 않는 사용자");

            return resultResponseDto;
        } else {
            project.setProjectName(projectRequestDto.getProjectName());
            project.setImageUrl(projectRequestDto.getImageUrl());
            project.setProjectDescription(projectRequestDto.getProjectDescription());
            project.setProjectDetail(projectRequestDto.getProjectDetail());
            project.setProjectStatus(projectRequestDto.getProjectStatus());
            project.setProjectPeriod(projectRequestDto.getProjectPeriod());
            project.setProjectCategory(projectRequestDto.getProjectCategory());
            project.setCreatedAt(currentTime);
            project.setUpdatedAt(currentTime);
            if(projectRequestDto.getBelongedClubId() == 0) {
                logger.info("[Log] 클럽 정보를 조회하지 못 함, ID:" + projectRequestDto.getBelongedClubId());
                project.setClub(null);
            } else {
                logger.info("[Log] 클럽 정보 조회 완료, ID:" + projectRequestDto.getBelongedClubId());
                project.setClub(clubDAO.selectClubById(projectRequestDto.getBelongedClubId()).get());
            }
            project.setUser(userDAO.selectUserById(projectRequestDto.getUploadedUserId()).get());

            Project createdProject = projectDAO.createProject(project);
            logger.info("[Log] 프로젝트 생성 됨, ID:" + createdProject.getId());

            for (ProjectCrewRequestDto projectCrewRequestDto : projectRequestDto.getProjectCrew()) {
                ProjectCrew projectCrew = new ProjectCrew();
                projectCrew.setProject(createdProject);
                projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
                projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
                projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
                projectCrew.setCreatedAt(currentTime);
                projectCrew.setUpdatedAt(currentTime);

                projectCrewDAO.createCrew(projectCrew);
                logger.info("[Log] 새로운 크루 생성됨:" + projectCrewRequestDto.getName() + "," + projectCrewRequestDto.getPosition() + "," + projectCrewRequestDto.getRole());
            }

            for (ProjectLinkRequestDto projectLinkRequestDto : projectRequestDto.getProjectLink()) {
                ProjectLink projectLink = new ProjectLink();
                projectLink.setLinkName(projectLinkRequestDto.getName());
                projectLink.setLinkUrl(projectLinkRequestDto.getLinkUrl());
                projectLink.setProject(createdProject);

                projectLinkDAO.createLink(projectLink);
                logger.info("[Log] 새로운 프로젝트 링크 생성됨:" + projectLinkRequestDto.getName() + "," + projectLinkRequestDto.getLinkUrl());
            }

            for (ProjectTechStackRequestDto projectTechStackRequestDto : projectRequestDto.getProjectTechStack()) {
                ProjectTechStack projectTechStack = new ProjectTechStack();
                projectTechStack.setTechStackName(projectTechStackRequestDto.getName());
                projectTechStack.setProject(createdProject);

                projectTechStackDAO.createTechStack(projectTechStack);
                logger.info("[Log] 새로운 프로젝트 기술 스텍 생성됨:" + projectTechStackRequestDto.getName());
            }

            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("프로젝트 생성 완료");

            return resultResponseDto;
        }
    }

    @Override
    public ResultResponseDto updateProject(Long projectId, ProjectRequestDto projectRequestDto) {
        LocalDateTime currentTime = LocalDateTime.now();

        Optional<Project> projectOptional = projectDAO.selectProjectById(projectId);
        if (!projectOptional.isPresent()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("프로젝트가 존재하지 않습니다.");
            logger.info("[Log] 프로젝트가 존재하지 않음, ID:" + projectId);
            return resultResponseDto;
        }

        Project project = projectOptional.get();
        project.setId(projectId);
        project.setProjectName(projectRequestDto.getProjectName());
        project.setImageUrl(projectRequestDto.getImageUrl());
        project.setProjectDescription(projectRequestDto.getProjectDescription());
        project.setProjectDetail(projectRequestDto.getProjectDetail());
        project.setProjectStatus(projectRequestDto.getProjectStatus());
        project.setProjectPeriod(projectRequestDto.getProjectPeriod());
        project.setProjectCategory(projectRequestDto.getProjectCategory());
        project.setUpdatedAt(currentTime);

        if(projectRequestDto.getBelongedClubId() == 0) {
            project.setClub(null);
            logger.info("[Log] 클럽 정보를 조회하지 못 함, ID:" + projectRequestDto.getBelongedClubId());
        } else {
            project.setClub(clubDAO.selectClubById(projectRequestDto.getBelongedClubId()).orElse(null));
        }

        project.setUser(userDAO.selectUserById(projectRequestDto.getUploadedUserId()).orElse(null));
        logger.info("[Log] 프로젝트 업데이트 됨");
        projectDAO.updateProject(project);

        logger.info("[Log] 프로젝트에 포함된 기술 스텍 전체 삭제");
        projectTechStackDAO.removeTechStack(projectId);
        logger.info("[Log] 프로젝트에 포함된 링크 전체 삭제");
        projectLinkDAO.removeLink(projectId);
        logger.info("[Log] 프로젝트에 포함된 크루 전체 삭제");
        projectCrewDAO.removeCrew(projectId);

        for (ProjectCrewRequestDto projectCrewRequestDto : projectRequestDto.getProjectCrew()) {
            ProjectCrew projectCrew = new ProjectCrew();
            projectCrew.setProject(project);
            projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
            projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
            projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
            projectCrew.setCreatedAt(currentTime);
            projectCrew.setUpdatedAt(currentTime);

            projectCrewDAO.createCrew(projectCrew);

            logger.info("[Log] 새로운 크루 생성됨:" + projectCrewRequestDto.getName() + "," + projectCrewRequestDto.getPosition() + "," + projectCrewRequestDto.getRole());
        }

        for (ProjectLinkRequestDto projectLinkRequestDto : projectRequestDto.getProjectLink()) {
            ProjectLink projectLink = new ProjectLink();
            projectLink.setLinkName(projectLinkRequestDto.getName());
            projectLink.setLinkUrl(projectLinkRequestDto.getLinkUrl());
            projectLink.setProject(project);

            projectLinkDAO.createLink(projectLink);

            logger.info("[Log] 새로운 프로젝트 링크 생성됨:" + projectLinkRequestDto.getName() + "," + projectLinkRequestDto.getLinkUrl());
        }

        for (ProjectTechStackRequestDto projectTechStackRequestDto : projectRequestDto.getProjectTechStack()) {
            ProjectTechStack projectTechStack = new ProjectTechStack();
            projectTechStack.setTechStackName(projectTechStackRequestDto.getName());
            projectTechStack.setProject(project);

            projectTechStackDAO.createTechStack(projectTechStack);

            logger.info("[Log] 새로운 프로젝트 기술 스텍 생성됨:" + projectTechStackRequestDto.getName());
        }

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("프로젝트 업데이트 완료");

        return resultResponseDto;
    }
}
