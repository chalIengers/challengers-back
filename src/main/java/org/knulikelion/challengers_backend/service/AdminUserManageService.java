package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.response.AdminDisableUserManageResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.AdminUserManageResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AdminUserManageService {
    ResponseEntity<Page<AdminUserManageResponseDto>> getAllUseAbleUser(Pageable pageable);
    ResponseEntity<Page<AdminDisableUserManageResponseDto>> getAllDisAbleUser(Pageable pageable);
    ResponseEntity<BaseResponseDto> unregisterUser(List<String> userEmailList);
    ResponseEntity<BaseResponseDto> reRegisterUser(List<String> userEmailList);
}
