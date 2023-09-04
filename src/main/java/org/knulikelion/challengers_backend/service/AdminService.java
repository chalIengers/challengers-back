package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.AssignAdministratorRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.NoticeRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.NoticeResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;

import java.util.List;

public interface AdminService {
    SignResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException;
    BaseResponseDto assignAdministrator(AssignAdministratorRequestDto assignAdministratorRequestDto);
    BaseResponseDto postNoti(NoticeRequestDto noticeRequestDto, String email);
    List<NoticeResponseDto> getAllNoti();
    NoticeResponseDto getNotiDetail(Long id);
}
