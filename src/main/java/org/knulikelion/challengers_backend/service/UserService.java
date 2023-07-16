package org.knulikelion.challengers_backend.service;


import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;

public interface UserService {
    Object getUserById(Long id);
    ResultResponseDto removeUser(Long id);
}
