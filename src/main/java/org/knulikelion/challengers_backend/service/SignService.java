package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestWithCodeDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignInResponseDto;

public interface SignService {
    ResultResponseDto signUp(SignUpRequestWithCodeDto signUpRequestWithCodeDto);
    ResultResponseDto sendCode(SignUpRequestDto signUpRequestDto);
    SignInResponseDto signIn(SignInRequestDto signInRequestDto);
}
