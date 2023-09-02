package org.knulikelion.challengers_backend.service.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e){
        // 에러 응답 객체 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message",e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleUSerNotFoundException(UserNotFoundException e){
        // 에러 응답 객체 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message",e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ClubNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleClubNotFoundException(ClubNotFoundException e){
        // 에러 응답 객체 생성
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message",e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
