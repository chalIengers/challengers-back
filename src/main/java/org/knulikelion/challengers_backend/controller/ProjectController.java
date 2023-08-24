package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return projectService.getProjectById(id);
    }

    @GetMapping("/get/all")
    public List<AllProjectResponseDto> getAllProjects() {
        return projectService.getAllProjects();
    }

    @DeleteMapping("/remove")
    public ResultResponseDto removeProjectById(Long id) {
        return projectService.removeProject(id);
    }

    @PostMapping("/create")
    public ResultResponseDto createProject(@RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.createProject(projectRequestDto);
    }

    @PutMapping("/update")
    public ResultResponseDto updateProject(@RequestBody ProjectRequestDto projectRequestDto, Long projectId) {
        return projectService.updateProject(projectId, projectRequestDto);
    }
}