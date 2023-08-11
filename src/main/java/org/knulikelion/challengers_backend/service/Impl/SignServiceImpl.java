package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.common.CommonResponse;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.response.SignInResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignUpResponseDto;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class SignServiceImpl implements SignService {
    public UserRepository userRepository;
    public JwtTokenProvider jwtTokenProvider;
    public PasswordEncoder passwordEncoder;
    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        log.info("[getSignUpResult] 회원 가입 정보 전달");
        User user;
        if(signUpRequestDto.getRole().equalsIgnoreCase("admin")){
            user = User.builder()
                    .userName(signUpRequestDto.getUserName())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .email(signUpRequestDto.getEmail())
                    .roles(Collections.singletonList("ROLE_ADMIN"))
                    .clubs(null)
                    .build();
        }else{
            user = User.builder()
                    .userName(signUpRequestDto.getUserName())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .email(signUpRequestDto.getEmail())
                    .roles(Collections.singletonList("ROLE_USER"))
                    .clubs(null)
                    .build();

        }

        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();

        if(userRepository.getByEmail(user.getEmail()) != null){
            signUpResponseDto.setSuccess(false);
            signUpResponseDto.setMsg("이미 가입된 회원");
            signUpResponseDto.setCode(1);
        }else{
            User savedUser = userRepository.save(user);
            log.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
            if(!savedUser.getEmail().isEmpty()){
                log.info("[getSignUpResult] 정상 처리 완료");
                setSuccessResult(signUpResponseDto);
            }else{
                log.info("[getSignUpResult] 실패 처리 완료");
                setFailResult(signUpResponseDto);
            }
        }

        return signUpResponseDto;
    }

    @Override
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) throws RuntimeException {
        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.getByEmail(signInRequestDto.getEmail());
        log.info("[getSignInResult] Email : {}", signInRequestDto.getEmail());

        log.info("[getSignInResult] 패스워드 비교 수행");
        if(!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())){
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");

        log.info("[getSignInResult] SignInResponseDto 객체 생성");
        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .token(jwtTokenProvider.createToken(user.getEmail(),user.getRoles())).build();

        log.info("[getSignInResult] SignInResponseDto 객체에 값 주입");
        return signInResponseDto;
    }

    // 결과 모델에 api 요청 성공 데이터를 세팅해주는 메소드
    private void setSuccessResult(SignUpResponseDto result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }

    // 결과 모델에 api 요청 실패 데이터를 세팅해주는 메소드
    private void setFailResult(SignUpResponseDto result) {
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMsg(CommonResponse.FAIL.getMsg());
    }
}
