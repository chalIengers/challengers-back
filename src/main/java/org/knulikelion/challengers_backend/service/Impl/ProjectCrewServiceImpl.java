package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.data.dao.ProjectCrewDAO;
import org.knulikelion.challengers_backend.data.dao.ProjectDAO;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.entity.ProjectCrew;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectCrewServiceImpl implements ProjectCrewService {
    private final ProjectDAO projectDAO;
    private final ProjectCrewDAO projectCrewDAO;

    private final ProjectCrewRepository projectCrewRepository;

    public ProjectCrewServiceImpl(ProjectDAO projectDAO, ProjectCrewDAO projectCrewDAO, ProjectCrewRepository projectCrewRepository) {
        this.projectDAO = projectDAO;
        this.projectCrewDAO = projectCrewDAO;
        this.projectCrewRepository = projectCrewRepository;
    }

    @Override
    public BaseResponseDto createProjectCrew(ProjectCrewRequestDto projectCrewRequestDto) {


        ProjectCrew projectCrew = new ProjectCrew();
        projectCrew.setProjectCrewName(projectCrewRequestDto.getName());
        projectCrew.setProjectCrewRole(projectCrewRequestDto.getRole());
        projectCrew.setProjectCrewPosition(projectCrewRequestDto.getPosition());
        projectCrew.setCreatedAt(LocalDateTime.now());
        projectCrew.setUpdatedAt(LocalDateTime.now());

        ProjectCrew createdProjectCrew = projectCrewDAO.createCrew(projectCrew);
        BaseResponseDto resultResponseDto = new BaseResponseDto();
        resultResponseDto.setSuccess(true);
        resultResponseDto.setMsg("팀원 생성 완료");

        return resultResponseDto;
    }

    @Override
    public Object getCrewsGroupedByPosition(Long id) {

        Optional<ProjectCrew> optionalProjectCrew = projectCrewDAO.selectById(id);

        if (!optionalProjectCrew.isPresent()) {
            BaseResponseDto responseDto = new BaseResponseDto();

            responseDto.setSuccess(false);
            responseDto.setMsg("팀원 조회 불가.");

            return responseDto;
        } else {
            ProjectCrew getProjectCrew = optionalProjectCrew.get();

            List<ProjectCrewResponseDto> crews = projectCrewRepository.findAllByProjectId(getProjectCrew.getId())
                    .stream()
                    .map(ProjectCrewResponseDto::new)
                    .collect(Collectors.toList());

            Map<String, List<ProjectCrewResponseDto>> crewGroupedByPosition =
                    crews.stream().collect(Collectors.groupingBy(ProjectCrewResponseDto::getPosition));

            ProjectCrewResponseDto projectCrewResponseDto = new ProjectCrewResponseDto();
            projectCrewResponseDto.setId(getProjectCrew.getId());
            projectCrewResponseDto.setName(getProjectCrew.getProjectCrewName());
            projectCrewResponseDto.setRole(getProjectCrew.getProjectCrewRole());
            projectCrewResponseDto.setPosition(getProjectCrew.getProjectCrewPosition());

            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("individual", projectCrewResponseDto);
            responseMap.put("grouped", crewGroupedByPosition);

            return responseMap;

        }
    }

    @Override
    public Object getProjectCrewById(Long id) {
        if (projectCrewDAO.selectById(id).isEmpty()) {
            BaseResponseDto resultResponseDto = new BaseResponseDto();

            resultResponseDto.setSuccess(false);
            resultResponseDto.setMsg("팀원 조회 불가");

            return resultResponseDto;
        } else {
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
    public BaseResponseDto updateProjectCrew(Long id, ProjectCrewRequestDto projectCrewRequestDto) {

        Optional<ProjectCrew> projectCrewOptional = projectCrewDAO.selectById(id);
        if(projectCrewOptional.isEmpty()) {
            BaseResponseDto resultResponseDto = new BaseResponseDto();
            resultResponseDto.setSuccess(false);
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

        BaseResponseDto resultResponseDto = new BaseResponseDto();
        resultResponseDto.setSuccess(true);
        resultResponseDto.setMsg("팀원 수정 완료");

        return resultResponseDto;
    }

    @Override
    public BaseResponseDto removeProjectCrew(Long id) {
        BaseResponseDto resultResponseDto = new BaseResponseDto();

        if(projectCrewDAO.selectById(id).isEmpty()) {
            resultResponseDto.setSuccess(false);
            resultResponseDto.setMsg("팀원 조회 불가");
        }else {
            projectCrewDAO.removeCrew(id);
            resultResponseDto.setSuccess(true);
            resultResponseDto.setMsg("팀원 삭제 완료");
        }
        return resultResponseDto;
    }
}
