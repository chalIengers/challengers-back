package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.AssignAdministratorRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.NoticeRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UpdateNoticeRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.NoticeResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;
import org.knulikelion.challengers_backend.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
