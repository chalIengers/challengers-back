package org.knulikelion.challengers_backend.service;


import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignInResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignUpResponseDto;

public interface SignService {
    SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto);
    SignInResponseDto signIn(SignInRequestDto signInRequestDto)throws RuntimeException;
}
