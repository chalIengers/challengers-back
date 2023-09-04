package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.UserRemoveRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.UserRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface UserService {
    Object getUserById(Long id);
    BaseResponseDto remove1User(String userEmail, UserRemoveRequestDto userRemoveRequestDto);
    ResultResponseDto updateUser(Long id, UserRequestDto userRequestDto) throws Exception;
}
