package org.knulikelion.challengers_backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestWithCodeDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
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
    @PostMapping(value = "/request-sign-up")
    public ResultResponseDto requestSignUp(@RequestBody SignUpRequestDto signUpRequestDto){
        return signService.sendCode(signUpRequestDto);
    }

    @PostMapping(value = "/sign-up")
    public ResultResponseDto singUp(@RequestBody SignUpRequestWithCodeDto signUpRequestWithCodeDto){
        return signService.signUp(signUpRequestWithCodeDto);
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
