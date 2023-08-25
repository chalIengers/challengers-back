package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.ApiOperation;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/project-crews")
public class ProjectCrewController {
    private final ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService, ProjectCrewRepository projectCrewRepository) {
        this.projectCrewService = projectCrewService;
    }

    @PostMapping
    @ApiOperation(value = "crew 추가")
    public ResultResponseDto createCrew(@RequestBody ProjectCrewRequestDto projectCrewResponseDto) {
        return projectCrewService.createProjectCrew(projectCrewResponseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "crew 조회")
    public Object getCrewById(@PathVariable Long id) {
        return projectCrewService.getProjectCrewById(id);
    }

    @GetMapping("/position/{projectId}}")
    @ApiOperation(value = "포지션 별 crew 조회")
    public Map<String, List<ProjectCrewResponseDto>> getCrewsGroupedByPosition(@PathVariable Long projectId) {
        List<ProjectCrewResponseDto> crews = (List<ProjectCrewResponseDto>) projectCrewService.getCrewsGroupedByPosition(projectId);
        return crews.stream()
                .collect(Collectors.groupingBy(ProjectCrewResponseDto::getPosition));
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "crew 수정")
    public ResultResponseDto updateCrewById(@RequestBody ProjectCrewRequestDto projectCrewRequestDto,@PathVariable Long id) {
        return projectCrewService.updateProjcetCrew(id,projectCrewRequestDto);
    }

    @DeleteMapping
    @ApiOperation(value = "crew 삭제")
    public ResultResponseDto removeCrewById(Long id) {
        return projectCrewService.removeProjectCrew(id);
    }
}
