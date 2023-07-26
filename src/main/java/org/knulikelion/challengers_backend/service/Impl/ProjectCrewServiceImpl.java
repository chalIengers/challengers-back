package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProjectCrewServiceImpl implements ProjectCrewService {
    private final ProjectDAO projectDAO;
    private final ProjectCrewDAO projectCrewDAO;

    public ProjectCrewServiceImpl(ProjectDAO projectDAO, ProjectCrewDAO projectCrewDAO) {
        this.projectDAO = projectDAO;
        this.projectCrewDAO = projectCrewDAO;
    }

    @Override
    public ResultResponseDto createProjectCrew(ProjectCrewRequestDto projectCrewRequestDto) {


        ProjectCrew projectCrew = new ProjectCrew();
        projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
        projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
        projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
        projectCrew.setCreatedAt(LocalDateTime.now());
        projectCrew.setUpdatedAt(LocalDateTime.now());

        ProjectCrew createdProjectCrew = projectCrewDAO.createCrew(projectCrew);
        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("팀원 생성 완료");

        return resultResponseDto;
    }

    @Override
    public Object getProjectCrewById(Long id) {
        if(projectCrewDAO.selectById(id).isEmpty()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();

            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("팀원 조회 불가");

            return resultResponseDto;
        }else {
            ProjectCrew getProjectCrew = projectCrewDAO.selectById(id).get();
            ProjectCrewResponseDto projectCrewResponseDto = new ProjectCrewResponseDto();
            projectCrewResponseDto.setId(getProjectCrew.getId());
            projectCrewResponseDto.setName(getProjectCrew.getProjectCrewName());
            projectCrewResponseDto.setRole(getProjectCrew.getProjectCrewRole());
            projectCrewResponseDto.setPosition(getProjectCrew.getProjectCrewPosition());

            return projectCrewResponseDto;
        }
    }

    @Override
    public ResultResponseDto updateProjcetCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto) {

        Optional<ProjectCrew> projectCrewOptional = projectCrewDAO.selectById(id);
        if(projectCrewOptional.isEmpty()) {
            ResultResponseDto resultResponseDto = new ResultResponseDto();
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("팀원이 존재하지 않습니다.");
            return resultResponseDto;
        }
        ProjectCrew projectCrew = projectCrewOptional.get();
        projectCrew.setId(id);
        projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
        projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
        projectCrew.setProjectCrewPosition(projectCrew.getProjectCrewPosition());
        projectCrew.setUpdatedAt(LocalDateTime.now());

        projectCrewDAO.updateCrew(projectCrew);

        ResultResponseDto resultResponseDto = new ResultResponseDto();
        resultResponseDto.setCode(0);
        resultResponseDto.setMsg("팀원 수정 완료");

        return resultResponseDto;
    }

    @Override
    public ResultResponseDto removeProjectCrew(Long id) {
        ResultResponseDto resultResponseDto = new ResultResponseDto();

        if(projectCrewDAO.selectById(id).isEmpty()) {
            resultResponseDto.setCode(1);
            resultResponseDto.setMsg("팀원 조회 불가");
        }else {
            projectCrewDAO.removeCrew(id);
            resultResponseDto.setCode(0);
            resultResponseDto.setMsg("팀원 삭제 완료");
        }
        return resultResponseDto;
    }
}
