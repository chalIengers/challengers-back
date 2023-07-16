package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/get")
    public Object getProjectById(Long id) {
        Object result = projectService.getProjectById(id);
        return result;
    }

    @DeleteMapping("/remove")
    public ResultResponseDto removeProjectById(Long id) {
        return projectService.removeProject(id);
    }

    @PostMapping("/create")
    public ResultResponseDto createProject(ProjectRequestDto projectRequestDto) {
        return projectService.createProject(projectRequestDto);
    }
}