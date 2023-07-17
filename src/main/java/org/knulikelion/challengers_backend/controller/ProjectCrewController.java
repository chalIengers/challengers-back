package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.ApiOperation;
import org.knulikelion.challengers_backend.data.dto.request.ProjectCrewRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ProjectCrewResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.repository.ProjectCrewRepository;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/projectCrew")
public class ProjectCrewController {
    final private ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService, ProjectCrewRepository projectCrewRepository) {
        this.projectCrewService = projectCrewService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "crew 추가")
    public ResultResponseDto createCrew(@RequestBody ProjectCrewRequestDto projectCrewResponseDto) {
        return projectCrewService.createProjectCrew(projectCrewResponseDto);
    }

    @GetMapping("/get")
    @ApiOperation(value = "crew 조회")
    public Object getCrewById(Long id) {
        Object result = projectCrewService.getProjectCrewById(id);
        return result;
    }

    /*@PutMapping("/update")
    @ApiOperation(value = "crew 수정")
    public ResponseEntity<ProjectCrewRequestDto> updateCrew(Long id, @RequestBody ProjectCrewRequestDto projectCrewRequestDto) throws Exception{
        projectCrewService.updateCrew(id,projectCrewRequestDto);
        ProjectCrewRequestDto updateCrewDto = projectCrewService.readCrew(id);

        return ResponseEntity.status(HttpStatus.OK).body(updateCrewDto);
    }*/

    @DeleteMapping("/remove")
    @ApiOperation(value = "crew 삭제")
    public ResultResponseDto removeCrewById(Long id) {
        return projectCrewService.removeProjectCrew(id);
    }
}
