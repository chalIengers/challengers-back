package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;
import org.knulikelion.challengers_backend.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    private final JwtTokenProvider jwtTokenProvider;

    public MyPageController(MyPageService myPageService, JwtTokenProvider jwtTokenProvider) {
        this.myPageService = myPageService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/info")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<MyPageResponseDto> getInfo(HttpServletRequest request) {
        return ResponseEntity.ok(myPageService.getInfo(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN"))));
    }
}