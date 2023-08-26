package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.ApiOperation;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/project-crews")
public class ProjectCrewController {
    private final ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService, ProjectCrewRepository projectCrewRepository) {
        this.projectCrewService = projectCrewService;
    }

    @PostMapping
    @ApiOperation(value = "crew 추가")
    public BaseResponseDto createCrew(@RequestBody ProjectCrewRequestDto projectCrewResponseDto) {
        return projectCrewService.createProjectCrew(projectCrewResponseDto);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "crew 조회")
    public Object getCrewById(@PathVariable Long id) {
        return projectCrewService.getProjectCrewById(id);
    }
    @GetMapping("/position/{projectId}")
    @ApiOperation(value = "포지션 별 crew 조회")
    public Map<String, Object> getCrewsGroupedByPosition(@PathVariable Long projectId) {
        return (Map<String, Object>) projectCrewService.getCrewsGroupedByPosition(projectId);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "crew 수정")
    public BaseResponseDto updateCrewById(@RequestBody ProjectCrewRequestDto projectCrewRequestDto,@PathVariable Long id) {
        return projectCrewService.updateProjectCrew(id,projectCrewRequestDto);
    }

    @DeleteMapping
    @ApiOperation(value = "crew 삭제")
    public BaseResponseDto removeCrewById(Long id) {
        return projectCrewService.removeProjectCrew(id);
    }
}
