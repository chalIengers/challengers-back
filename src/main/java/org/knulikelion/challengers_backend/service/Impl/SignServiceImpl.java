package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestWithCodeDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignUpResponseDto;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.MailService;
import org.knulikelion.challengers_backend.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Map<String, String> emailCodeMap = new HashMap<>();
    private final String domain = "@kangnam.ac.kr";


    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }


    @Override
    public ResultResponseDto sendCode(SignUpRequestDto signUpRequestDto) {
        log.info("[sendCode] 인증 번호 전송 Email : {}", signUpRequestDto.getEmail());
        String approvalNumber = mailService.sendMail(signUpRequestDto.getEmail() + domain);
        log.info("[sendCode] 인증 번호 전송 완료 Email : {}", signUpRequestDto.getEmail());
        emailCodeMap.put(signUpRequestDto.getEmail() + domain, approvalNumber);

        ResultResponseDto resultResponseDto = ResultResponseDto.builder()
                .code(1)
                .msg("인증번호를 발송하였습니다.")
                .build();

        return resultResponseDto;
    }

    @Override
    public SignUpResponseDto signUp(SignUpRequestWithCodeDto signUpRequestWithCodeDto) {
        log.info("[verifyCode] 인증 번호 확인");
        String email = signUpRequestWithCodeDto.getEmail() + domain;
        String userName = signUpRequestWithCodeDto.getUserName();
        String password = signUpRequestWithCodeDto.getPassword();
        String inputNumber = signUpRequestWithCodeDto.getInputNumber();
        String actualNumber = emailCodeMap.get(signUpRequestWithCodeDto.getEmail()+domain);

        log.info("emailCodeMap : " + emailCodeMap.values());

        User user;
        SignUpResponseDto signUpResponseDto = new SignUpResponseDto();

        if (inputNumber != null && actualNumber != null && actualNumber.equals(inputNumber)) {
            user = User.builder()
                    .userName(userName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            userRepository.save(user);

            signUpResponseDto.setSuccess(true);
            signUpResponseDto.setMsg("성공적으로 회원가입 하였습니다.");
            signUpResponseDto.setCode(1);
            return signUpResponseDto;
        } else {
            signUpResponseDto.setSuccess(false);
            signUpResponseDto.setMsg("회원가입에 실패하셨습니다.");
            signUpResponseDto.setCode(0);
            return signUpResponseDto;
        }
    }
}
