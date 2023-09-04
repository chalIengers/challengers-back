package org.knulikelion.challengers_backend.service;

import org.knulikelion.challengers_backend.data.dto.request.UserRemoveRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.MyPageResponseDto;

public interface MyPageService {
    MyPageResponseDto getInfo(String email);
    BaseResponseDto unRegister(String email, UserRemoveRequestDto userRemoveRequestDto);
}
