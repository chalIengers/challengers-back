package org.knulikelion.challengers_backend.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import org.knulikelion.challengers_backend.data.dto.ProjectCrewDto;
import org.knulikelion.challengers_backend.service.ProjectCrewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Controller
@RequestMapping("/api/crud/projectCrew")
public class ProjectCrewController {
    final private ProjectCrewService projectCrewService;

    public ProjectCrewController(ProjectCrewService projectCrewService) {
        this.projectCrewService = projectCrewService;
    }

    @PostMapping
    @ApiOperation(value = "crew 추가")
    public ResponseEntity<ProjectCrewDto.Info> createCrew(@RequestBody ProjectCrewDto.ProjectCrewRequestDto projectCrewRequest){
        ProjectCrewDto.Info createCrewDto = projectCrewService.createCrew(projectCrewRequest);

        return ResponseEntity.status(HttpStatus.OK).body(createCrewDto);
    }

    @GetMapping
    @ApiOperation(value = "crew 조회")
    public ResponseEntity<ProjectCrewDto.Info> readCrew(Long id){
        ProjectCrewDto.Info readCrewDto = projectCrewService.readCrew(id);

        return ResponseEntity.status(HttpStatus.OK).body(readCrewDto);
    }

    @PutMapping
    @ApiOperation(value = "crew 수정")
    public ResponseEntity<ProjectCrewDto.Info> udpateCrew(Long id, @RequestBody ProjectCrewDto.ProjectCrewRequestDto projectCrewRequestDto) throws Exception{
        projectCrewService.updateCrew(id,projectCrewRequestDto);
        ProjectCrewDto.Info updateCrewDto = projectCrewService.readCrew(id);

        return ResponseEntity.status(HttpStatus.OK).body(updateCrewDto);
    }

    @DeleteMapping
    @ApiOperation(value = "crew 삭제")
    public ResponseEntity<String> deleteCrew(Long id) throws Exception {
        projectCrewService.deleteCrew(id);

        return ResponseEntity.status(HttpStatus.OK).body("삭제 완료");
    }
}
