package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;

public interface AdminService {
    SignResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;
}
