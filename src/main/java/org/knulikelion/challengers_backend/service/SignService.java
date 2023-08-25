package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestWithCodeDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface SignService {
    ResultResponseDto signUp(SignUpRequestWithCodeDto signUpRequestWithCodeDto);
    ResultResponseDto sendCode(SignUpRequestDto signUpRequestDto);
}
