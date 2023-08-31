package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.ApiOperation;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/project-crews")
public class ProjectCrewController {
    private final ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService) {
        this.projectCrewService = projectCrewService;
    }

    @GetMapping("/{crewId}")
    @ApiOperation(value = "crew 조회")
    public Object getCrewById(@PathVariable Long crewId) {
        return projectCrewService.getProjectCrewById(crewId);
    }

    @GetMapping("/position/{projectId}")
    @ApiOperation(value = "포지션 별 crew 조회")
    public Map<String, Object> getCrewsGroupedByPosition(@PathVariable Long projectId) {
        return (Map<String, Object>) projectCrewService.getCrewsGroupedByPosition(projectId);
    }

    @PutMapping("/{crewId}")
    @ApiOperation(value = "crew 수정")
    public BaseResponseDto updateCrewById(@RequestBody ProjectCrewRequestDto projectCrewRequestDto,@PathVariable Long crewId) {
        return projectCrewService.updateProjectCrew(crewId,projectCrewRequestDto);
    }

    @DeleteMapping
    @ApiOperation(value = "crew 삭제")
    public BaseResponseDto removeCrewById(Long crewId) {
        return projectCrewService.removeProjectCrew(crewId);
    }
}
