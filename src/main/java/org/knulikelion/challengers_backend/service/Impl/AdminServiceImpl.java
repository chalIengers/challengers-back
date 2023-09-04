package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.AssignAdministratorRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;
import org.knulikelion.challengers_backend.data.entity.ExtraUserMapping;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.ExtraUserMappingRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final ExtraUserMappingRepository extraUserMappingRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, ExtraUserMappingRepository extraUserMappingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.extraUserMappingRepository = extraUserMappingRepository;
    }

    @Override
    public SignResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {
        User user = userRepository.getByEmail(signInRequestDto.getEmail());

        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            return SignResponseDto.builder()
                    .success(false)
                    .msg("비밀번호가 일치하지 않습니다.")
                    .build();
        }

        if (!user.getRoles().contains("ROLE_ADMIN")) {
            return SignResponseDto.builder()
                    .success(false)
                    .msg("권한이 없는 사용자입니다.")
                    .build();
        }

        return SignResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(String.valueOf(user.getEmail()), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(String.valueOf(user.getEmail())))
                .email(user.getEmail())
                .uid(user.getUserName())
                .success(true)
                .msg("성공적으로 로그인되었습니다.")
                .build();
    }

    @Override
    public BaseResponseDto assignAdministrator(AssignAdministratorRequestDto assignAdministratorRequestDto) {
        User user = userRepository.getByEmail(assignAdministratorRequestDto.getEmail());
        BaseResponseDto baseResponseDto = new BaseResponseDto();

//        사용자를 찾을 수 없을 때
        if(user == null) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("해당 사용자를 찾을 수 없습니다.")
                    .build();
        }

//        이미 사용자가 관리자 권한을 가지고 있을 때
        if(user.getRoles().contains("ROLE_ADMIN")) {
            return BaseResponseDto.builder()
                    .success(false)
                    .msg("이미 관리자 권한을 가지고 있는 사용자입니다.")
                    .build();
        }

//        사용자에게 관리자 권한 추가
        user.getRoles().add("ROLE_ADMIN");

        ExtraUserMapping extraUserMapping = new ExtraUserMapping();
        extraUserMapping.setUser(user);
        extraUserMapping.setRole(assignAdministratorRequestDto.getRole());
        extraUserMapping.setProfileUrl(assignAdministratorRequestDto.getProfileUrl());

        extraUserMappingRepository.save(extraUserMapping);

        return BaseResponseDto.builder()
                .success(true)
                .msg("관리자 권한 추가가 완료되었습니다.")
                .build();
    }
}
