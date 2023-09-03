package org.knulikelion.challengers_backend.service.Impl;

import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignResponseDto;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
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
}
