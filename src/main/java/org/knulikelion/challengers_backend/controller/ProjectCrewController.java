package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.ApiOperation;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/project-crews")
public class ProjectCrewController {
    final private ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService, ProjectCrewRepository projectCrewRepository) {
        this.projectCrewService = projectCrewService;
    }

    @PostMapping
    @ApiOperation(value = "crew 추가")
    public ResultResponseDto createCrew(@RequestBody ProjectCrewRequestDto projectCrewResponseDto) {
        return projectCrewService.createProjectCrew(projectCrewResponseDto);
    }

    @GetMapping
    @ApiOperation(value = "crew 조회")
    public Object getCrewById(Long id) {
        Object result = projectCrewService.getProjectCrewById(id);
        return result;
    }

    @PutMapping
    @ApiOperation(value = "crew 수정")
    public ResultResponseDto updateCrewById(@RequestBody ProjectCrewRequestDto projectCrewRequestDto, Long id) {
        return projectCrewService.updateProjcetCrew(id,projectCrewRequestDto);
    }

    @DeleteMapping
    @ApiOperation(value = "crew 삭제")
    public ResultResponseDto removeCrewById(Long id) {
        return projectCrewService.removeProjectCrew(id);
    }
}
