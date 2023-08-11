package org.knulikelion.challengers_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignInResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignUpResponseDto;
import org.knulikelion.challengers_backend.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SignController {
    private final SignService signService;
    @Autowired
    public SignController(SignService signService) {
        this.signService = signService;
    }

    @PostMapping(value = "/sign-in")
    public SignInResponseDto signIn(@RequestBody SignInRequestDto signInRequestDto)throws RuntimeException{
        log.info("[signIn] 로그인을 시도하고 있습니다. Email : {}, pw : ****", signInRequestDto.getEmail());
        SignInResponseDto signInResponseDto = signService.signIn(signInRequestDto);
        if(signInResponseDto.getCode() == 0){
            log.info("[signIn] 정상적으로 로그인되었습니다. Email : {}, Token : {}", signInRequestDto.getEmail(), signInResponseDto.getToken());
        }
        return signInResponseDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResponseDto singUp(@RequestBody SignUpRequestDto signUpRequestDto){
        log.info("[signUp] 회원가입을 수행합니다. Email : {}, password : ****, name : {}, role : {}",
                signUpRequestDto.getEmail(), signUpRequestDto.getUserName(), signUpRequestDto.getRole());
        SignUpResponseDto signUpResponseDto = signService.signUp(signUpRequestDto);
        log.info("[signUp] 회원가입을 완료했습니다. Email : {}", signUpRequestDto.getEmail());
        return signUpResponseDto;
    }

    @GetMapping(value = "/exception")
    public void exceptionTest() throws RuntimeException{
        throw new RuntimeException("접근이 금지되었습니다.");
    }
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> ExceptionHandler(RuntimeException e){
        HttpHeaders responseHeaders = new HttpHeaders();
        //responseHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        log.info("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());
        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code", "400");
        map.put("message", "에러 발생");

        return new ResponseEntity<>(map, responseHeaders,httpStatus);
    }
}
