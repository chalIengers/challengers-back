package org.knulikelion.challengers_backend.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.ChangePasswordRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.ChangePasswordWithCodeRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserRemoveRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.UnregisterValidateResponseDto;
import org.knulikelion.challengers_backend.service.MyPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @PostMapping("/change-Password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> changPassword(HttpServletRequest request, @RequestBody ChangePasswordWithCodeRequestDto changePasswordWithCodeRequestDto) {
        return ResponseEntity.ok(myPageService.changePassword(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),changePasswordWithCodeRequestDto));
    }

    @PostMapping("/sendCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<BaseResponseDto> sendPwChangeCode(HttpServletRequest request,@RequestBody ChangePasswordRequestDto changePasswordRequestDto){
        return ResponseEntity.ok(myPageService.sendPwChangeCode(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),changePasswordRequestDto));
    }

    @GetMapping("/verify-password")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<Boolean> verifyPassword(HttpServletRequest request,String password) {
        return ResponseEntity.ok(myPageService.checkPassword(jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")), password));
    }

    @GetMapping("/validate-unregister")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<UnregisterValidateResponseDto> validateUnregister(HttpServletRequest request) {
        return ResponseEntity.ok(myPageService.validateUnRegister(jwtTokenProvider.getUserEmail(
                request.getHeader("X-AUTH-TOKEN"))));
    }
      
    @DeleteMapping("/unregister")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "사용자 인증 Token", required = true, dataType = "String", paramType = "header")
    })
    public ResponseEntity<UnregisterValidateResponseDto> unRegister(HttpServletRequest request, @RequestBody UserRemoveRequestDto userRemoveRequestDto) {
        return ResponseEntity.ok(myPageService.unRegister(
                jwtTokenProvider.getUserEmail(request.getHeader("X-AUTH-TOKEN")),
                userRemoveRequestDto
        ));
    }
}