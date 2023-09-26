package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.*;
import org.knulikelion.challengers_backend.data.dto.response.*;
import org.knulikelion.challengers_backend.data.enums.ProjectStatus;
import org.knulikelion.challengers_backend.service.AdminService;
import org.knulikelion.challengers_backend.service.AdminUserManageService;
import org.knulikelion.challengers_backend.service.ClubService;
import org.knulikelion.challengers_backend.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final ClubService clubService;
    private final ProjectService projectService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminUserManageService adminUserManageService;

    public AdminController(AdminService adminService, ClubService clubService,
                           ProjectService projectService, JwtTokenProvider jwtTokenProvider, AdminUserManageService adminUserManageService) {
        this.adminService = adminService;
        this.clubService = clubService;
        this.projectService = projectService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.adminUserManageService = adminUserManageService;
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
        return clubService.getClubDetailById(id);
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

    @PostMapping(value = "/club/approve")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changeClubStatus(
            @RequestParam Long clubId,
            @RequestParam(defaultValue = "ACCEPT") String status) {
        return ResponseEntity.ok(adminService.changeClubStatus(clubId, status));
    }

    @DeleteMapping(value = "/club/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> removeClub(@RequestParam Long clubId) {
        return ResponseEntity.ok(clubService.removeClub(clubId));
    }

    @GetMapping(value = "/club/member")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<?> getAllClubMember(@RequestParam Long clubId) {
        return ResponseEntity.ok(clubService.getMembersByClubId(clubId));
    }

    @DeleteMapping(value = "/club/member/remove")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> removeClubMember(
            @RequestParam(required = true) Long clubId,
            @RequestParam(required = true) List<Long> userId
    ) {
        return ResponseEntity.ok(adminService.removeClubMember(clubId, userId));
    }
      
//  대시 보드
    @GetMapping("/count/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getUsersCounts() {
        return ResponseEntity.ok(adminService.countUsers());
    }

    @GetMapping("/count/clubs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getClubsCounts() {
        return ResponseEntity.ok(adminService.countClubs());
    }

    @GetMapping("/count/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getProjectsCounts() {
        return ResponseEntity.ok(adminService.countProjects());
    }

    @GetMapping("/count/today/add/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayAddProjects() {
        return ResponseEntity.ok(adminService.countTodayAddProjects());
    }

    @GetMapping("/count/today/add/clubs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayAddClubs() {
        return ResponseEntity.ok(adminService.countTodayAddClubs());
    }

    @GetMapping("/count/today/add/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayAddUsers() {
        return ResponseEntity.ok(adminService.countTodayAddUsers());
    }

    @GetMapping("/count/today/deleted/projects")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayDeletedProjects() {
        return ResponseEntity.ok(adminService.countTodayDeletedProjects());
    }

    @GetMapping("/count/today/deleted/clubs")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayDeletedClubs() {
        return ResponseEntity.ok(adminService.countTodayDeletedClubs());
    }

    @GetMapping("/count/today/deleted/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getTodayDeletedUsers() {
        return ResponseEntity.ok(adminService.countTodayDeletedUsers());
    }

    @GetMapping("/count/all/deleted/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Long> getAllDeletedUsers() {
        return ResponseEntity.ok(adminService.countDeletedUsers());
    }

    // 유저 관리페이지
    @GetMapping("/get-useAble/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Page<AdminUserManageResponseDto>> getAllUseAbleUsers(@RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by("id").ascending());
        return adminUserManageService.getAllUseAbleUser(pageable);
    }

    @GetMapping("/get-disAble/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Page<AdminDisableUserManageResponseDto>> getAllDisAbleUsers(@RequestParam(defaultValue = "0") int page){
        Pageable pageable = PageRequest.of(page,10, Sort.by("id").ascending());
        return adminUserManageService.getAllDisAbleUser(pageable);
    }

    @DeleteMapping ("/deactivate/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> deActivateUser(@RequestBody List<String> userEmailList){
        return adminUserManageService.unregisterUser(userEmailList);
    }

    @PostMapping("/reactivate/users")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> reActivateUser(@RequestBody List<String> userEmailList){
        return adminUserManageService.reRegisterUser(userEmailList);
    }

//  홈피드

    @PostMapping("/home/feed/create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> createHomeFeed(@RequestBody AdminHomeFeedRequestDto adminHomeFeedDto, HttpServletRequest request) {
        return ResponseEntity.ok(adminService.createHomeFeed(adminHomeFeedDto,request.getHeader("X-AUTH-TOKEN")));
    }

    @GetMapping("/home/feed/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Object> getOneHomeFeed(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getHomeFeed(id));
    }

    @GetMapping("/home/feed/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<List<AdminHomeFeedResponseDto>> getAllHomeFeeds() {
        return ResponseEntity.ok(adminService.getAllHomeFeed());
    }

    @PutMapping("/home/feed/update/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> updateHomeFeed(AdminHomeFeedRequestDto adminHomeFeedRequestDto,@PathVariable Long id) {
        return ResponseEntity.ok(adminService.updateHomeFeed(adminHomeFeedRequestDto,id));
    }

    @DeleteMapping("/home/feed/delete/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> deleteHomeFeed(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteHomeFeed(id));
    }



   @GetMapping("/latest/created/projects")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
    public ResponseEntity<List<ProjectAuditResponseDto>> getLatestCreatedProject() {
        return ResponseEntity.ok(adminService.getLatestCreatedProject());
   }

   @GetMapping("/latest/deleted/projects")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
   public ResponseEntity<List<ProjectAuditResponseDto>> getLatestDeletedProjects() {
        return ResponseEntity.ok(adminService.getLatestDeletedProject());
   }

   @GetMapping("/latest/created/clubs")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
   public ResponseEntity<List<ClubAuditResponseDto>> getLatestCreatedClubs() {
        return ResponseEntity.ok(adminService.getLatestCreatedClub());
   }

   @GetMapping("/latest/deleted/clubs")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
   public ResponseEntity<List<ClubAuditResponseDto>> getLatestDeletedClubs() {
        return ResponseEntity.ok(adminService.getLatestDeletedClub());
   }
   @GetMapping("/latest/created/users")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
    public ResponseEntity<List<UserAuditResponseDto>> getLatestCreatedUsers() {
        return ResponseEntity.ok(adminService.getLatestCreatedUser());
   }

   @GetMapping("/latest/deleted/users")
   @ApiImplicitParams({
           @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
   })
    public ResponseEntity<List<UserAuditResponseDto>> getLatestDeletedUsers() {
        return ResponseEntity.ok(adminService.getLatestDeletedUser());
   }

}
