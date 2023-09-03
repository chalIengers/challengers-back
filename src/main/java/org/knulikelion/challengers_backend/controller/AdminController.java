package org.knulikelion.challengers_backend.controller;

import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;
import org.knulikelion.challengers_backend.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(value = "/sign")
    public SignResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {
        return adminService.signIn(signInRequestDto);
    }
}
