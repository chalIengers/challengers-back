package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.*;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;
import org.knulikelion.challengers_backend.service.AdminService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final ClubService clubService;
    private final ProjectService projectService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(AdminService adminService, ClubService clubService,
                           ProjectService projectService, JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.clubService = clubService;
        this.projectService = projectService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/sign")
    public ResponseEntity<SignResponseDto> signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {
        return ResponseEntity.ok(adminService.signIn(signInRequestDto));
    }

    @PostMapping(value = "/set")
    public ResponseEntity<BaseResponseDto> assignAdministrator(@RequestBody AssignAdministratorRequestDto assignAdministratorRequestDto) {
        return ResponseEntity.ok(adminService.assignAdministrator(assignAdministratorRequestDto));
    }

    @PostMapping(value = "/notice/post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> postNoti(@RequestBody NoticeRequestDto noticeRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(adminService.postNoti(noticeRequestDto, jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN"))));
    }

    @GetMapping(value = "/notice/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<NoticeResponseDto>> getAllNoti() {
        return ResponseEntity.ok(adminService.getAllNoti());
    }

    @GetMapping(value = "/notice/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<NoticeResponseDto> getNotiDetail(Long id) {
        return ResponseEntity.ok(adminService.getNotiDetail(id));
    }

    @DeleteMapping(value = "/notice/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> deleteNoti(Long id) {
        return ResponseEntity.ok(adminService.deleteNoti(id));
    }

    @PutMapping(value = "/notice/update")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> updateNoti(@RequestBody UpdateNoticeRequestDto updateNoticeRequestDto, HttpServletRequest request) {
        return ResponseEntity.ok(adminService.updateNoti(updateNoticeRequestDto, jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN"))));
    }

    @PostMapping(value = "/setting/password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changePw(@RequestBody ChangePasswordRequestDto changePasswordRequestDto,
                                                    HttpServletRequest request) {
        return ResponseEntity.ok(adminService.changePw(
                changePasswordRequestDto,
                jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN"))
        ));
    }

    @PostMapping(value = "/setting/role")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changeRole(String role, HttpServletRequest request) {
        return ResponseEntity.ok(adminService.changeRole(
                role,
                jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN"))
        ));
    }

    @PostMapping(value = "/setting/name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changeName(HttpServletRequest request, String name) {
        return ResponseEntity.ok(adminService.changeName(
                jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),
                name
        ));
    }

    @PostMapping(value = "/setting/profile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changeProfileUrl(HttpServletRequest request, String url) {
        return ResponseEntity.ok(adminService.changeProfile(
                jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),
                url
        ));
    }

    @GetMapping(value = "/club/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Page<AdminClubResponseDto>> getAllClub(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return ResponseEntity.ok(adminService.getAllClubs(page, size));
    }

    @GetMapping(value = "/club/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<ClubResponseDto> getClubDetail(Long id) {
        return ResponseEntity.ok(clubService.getClubDetailById(id));
    }

    @PutMapping(value = "/project/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changeProjectStatus(Long projectId, ProjectStatus status) {
        return ResponseEntity.ok(adminService.changeProjectStatus(projectId, status));
    }

    @DeleteMapping(value = "/project/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> forceDeleteProject(Long projectId) {
        return ResponseEntity.ok(projectService.removeProject(projectId));
    }

    @GetMapping(value = "/project/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Page<AllProjectResponseDto>> getAllProject(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        return ResponseEntity.ok(adminService.getAllProject(page, size));
    }

//  대시 보드
    @GetMapping("/count/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getUsersCount() {
        return ResponseEntity.ok(adminService.countUsers());
    }

    @GetMapping("/count/clubs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getClubsCount() {
        return ResponseEntity.ok(adminService.countClubs());
    }

    @GetMapping("/count/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getProjectsCount() {
        return ResponseEntity.ok(adminService.countProjects());
    }

    @GetMapping("/count/today/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayProjects() {
        return ResponseEntity.ok(adminService.countTodayProjects());
    }

    @GetMapping("/count/today/clubs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayClubs() {
        return ResponseEntity.ok(adminService.countTodayClubs());
    }

    @GetMapping("/count/today/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayUsers() {
        return ResponseEntity.ok(adminService.countTodayUsers());
    }

    @GetMapping("/deleted/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getDeletedProjects() {
        return ResponseEntity.ok(adminService.countDeletedProjects());
    }

}
