package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserUpdateRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface UserService {
    // JWT 보완 후 구현 예정
    Object getUserById(Long id);
    ResultResponseDto removeUser(Long id);
    ResultResponseDto createUser(UserUpdateRequestDto userUpdateRequestDto);
    ResultResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) throws Exception;
}
