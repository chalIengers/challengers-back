package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.AssignAdministratorRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.NoticeRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.NoticeResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;
import org.knulikelion.challengers_backend.service.AdminService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminController(AdminService adminService, JwtTokenProvider jwtTokenProvider) {
        this.adminService = adminService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping(value = "/sign")
    public SignResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {
        return adminService.signIn(signInRequestDto);
    }

    @PostMapping(value = "/set")
    public BaseResponseDto assignAdministrator(@RequestBody AssignAdministratorRequestDto assignAdministratorRequestDto) {
        return adminService.assignAdministrator(assignAdministratorRequestDto);
    }

    @PostMapping(value = "/notice/post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public BaseResponseDto postNoti(@RequestBody NoticeRequestDto noticeRequestDto, HttpServletRequest request) {
        return adminService.postNoti(noticeRequestDto, jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")));
    }

    @GetMapping(value = "/notice/all")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public List<NoticeResponseDto> getAllNoti() {
        return adminService.getAllNoti();
    }

    @GetMapping(value = "/notice/get")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public NoticeResponseDto getNotiDetail(Long id) {
        return adminService.getNotiDetail(id);
    }
}
