package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.data.dto.request.ProjectRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.AllProjectTechStacksResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectTechStackResponseDto;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/project")
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
    public ResponseEntity<Page<AllProjectResponseDto>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "ALL") String categories,
            @RequestParam(required = false) List<String> techStack,
            @RequestParam(defaultValue = "NEW") String sort) {
        if (techStack == null) {
            techStack = new ArrayList<>();
        }

        Page<AllProjectResponseDto> result = projectService.getAllProject(page, size, categories, sort, techStack);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto removeProjectById(Long id) {
        return projectService.removeProject(id);
    }

    @PostMapping("/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto createProject(@RequestBody ProjectRequestDto projectRequestDto, HttpServletRequest request) {
        return projectService.createProject(projectRequestDto, request.getHeader("X-AUTH-TOKEN"));
    }

    @PutMapping("/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto updateProject(@RequestBody ProjectRequestDto projectRequestDto, Long projectId, HttpServletRequest request) {
        return projectService.updateProject(projectId, projectRequestDto, request.getHeader("X-AUTH-TOKEN"));
    }

    @GetMapping("/get/all/top-viewed/{year}/{month}")
        public Page<AllProjectResponseDto> getTopViewedProjectsInMonth(
                @PathVariable int year,
                @PathVariable int month,
                @RequestParam(defaultValue = "0")int page,
                @RequestParam(defaultValue = "6") int size) {
        YearMonth yearMonth = YearMonth.of(year,month);
        Pageable pageable= PageRequest.of(page,size);
        return projectService.getProjectsInMonth(yearMonth,pageable);
    }

    @GetMapping("/tech-stacks")
    public ResponseEntity<List<AllProjectTechStacksResponseDto>> getAllProjectTechStacks() {
        List<AllProjectTechStacksResponseDto> techStacks = projectService.getProjectTechStacks();
        return ResponseEntity.ok(techStacks);
    }
}