package org.knulikelion.challengers_backend.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.knulikelion.challengers_backend.config.security.JwtTokenProvider;
import org.knulikelion.challengers_backend.data.dto.request.SignInRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestDto;
import org.knulikelion.challengers_backend.data.dto.request.SignUpRequestWithCodeDto;
import org.knulikelion.challengers_backend.data.dto.response.BaseResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.ResultResponseDto;
import org.knulikelion.challengers_backend.data.dto.response.SignInResponseDto;
import org.knulikelion.challengers_backend.data.entity.EmailVerification;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.EmailVerificationRepository;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.knulikelion.challengers_backend.service.MailService;
import org.knulikelion.challengers_backend.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SignServiceImpl implements SignService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationRepository emailVerificationRepository;
    private final MailService mailService;
    private final String domain = "@kangnam.ac.kr";


    @Autowired
    public SignServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder, EmailVerificationRepository emailVerificationRepository, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.emailVerificationRepository = emailVerificationRepository;
        this.mailService = mailService;
    }

    @Override
    public BaseResponseDto verifyEmail(String email) {
        User findUser = userRepository.findByEmail(email+domain);
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        if(findUser != null){
            baseResponseDto.setSuccess(false);
            baseResponseDto.setMsg("이미 존재하는 계정입니다.");
        }else{
            baseResponseDto.setSuccess(true);
            baseResponseDto.setMsg("회원 가입이 가능합니다.");
        }
        return baseResponseDto;
    }
    @Override
    public BaseResponseDto sendCode(SignUpRequestDto signUpRequestDto) {
        BaseResponseDto baseResponseDto = new BaseResponseDto();

        List<EmailVerification> checkEmail = emailVerificationRepository.findByEmail(signUpRequestDto.getEmail()+domain);
        if(!checkEmail.isEmpty()){
            for(EmailVerification temp : checkEmail) {
                emailVerificationRepository.delete(temp);
            }
        }

        log.info("[sendCode] 인증 번호 전송 Email : {}", signUpRequestDto.getEmail()+domain);
        String approvalNumber = mailService.sendMail(signUpRequestDto.getEmail() + domain);
        log.info("[sendCode] 인증 번호 전송 완료 Email : {}, number : {}", signUpRequestDto.getEmail(), approvalNumber);

        EmailVerification emailVerification = EmailVerification.builder()
                .email(signUpRequestDto.getEmail()+domain)
                .code(approvalNumber)
                .expireTime(LocalDateTime.now().plusMinutes(5)) /*현재 시간으로부터 5분 뒤*/
                .build();

        emailVerificationRepository.save(emailVerification);
        log.info("[sendCode] DataBase 저장 완료");

        baseResponseDto.setSuccess(true);
        baseResponseDto.setMsg("성공적으로 인증 번호를 전송하였습니다.");

        return baseResponseDto;
    }



    @Override
    public ResultResponseDto signUp(SignUpRequestWithCodeDto signUpRequestWithCodeDto) {
        log.info("[verifyCode] 인증 번호 확인");

        if(signUpRequestWithCodeDto.getEmail()==null || signUpRequestWithCodeDto.getEmail().equals("null")){
            return ResultResponseDto.builder()
                    .code(0)
                    .msg("이메일을 입력해주세요.")
                    .build();
        }
        if(signUpRequestWithCodeDto.getPassword()==null || signUpRequestWithCodeDto.getPassword().equals("null")){
            return ResultResponseDto.builder()
                    .code(0)
                    .msg("비밀번호를 입력해주세요.")
                    .build();
        }
        if(signUpRequestWithCodeDto.getInputNumber()==null || signUpRequestWithCodeDto.getInputNumber().equals("null")){
            return ResultResponseDto.builder()
                    .code(0)
                    .msg("인증번호를 입력해주세요.")
                    .build();
        }

        String email = signUpRequestWithCodeDto.getEmail() + domain;
        String userName = signUpRequestWithCodeDto.getUserName();
        String password = signUpRequestWithCodeDto.getPassword();
        String inputNumber = signUpRequestWithCodeDto.getInputNumber();

        if (userRepository.findByEmail(email) != null) {
            return ResultResponseDto.builder()
                    .code(0)
                    .msg("중복된 이메일이 있습니다.")
                    .build();
        }

        List<EmailVerification> verification = emailVerificationRepository.findByEmail(email);

            if (!verification.isEmpty()) {
                EmailVerification emailVerification = verification.get(0);
                log.info("[emailVerification] : {}",emailVerification);

                if (LocalDateTime.now().isAfter(emailVerification.getExpireTime())) {
                    log.info("[signUp] 이메일 인증번호 만료");
                    log.info("[signUp] 이메일 인증번호 삭제");
                    emailVerificationRepository.delete(emailVerification);

                    return ResultResponseDto.builder()
                            .code(0)
                            .msg("인증 번호가 만료되었습니다. 재발급 하세요.")
                            .build();
                } else if (!inputNumber.equals(emailVerification.getCode()) || emailVerification.getCode()==null) {
                    log.info("[signUp] 이메일 인증번호 불일치");

                    return ResultResponseDto.builder()
                            .code(0)
                            .msg("인증 번호가 일치하지 않습니다.")
                            .build();
                } else if(!emailVerification.getEmail().equals(email)){
                    log.info("[signUp] 이메일 불일치");

                    return ResultResponseDto.builder()
                            .code(0)
                            .msg("이메일이 일치하지 않습니다.")
                            .build();
                }

                User user = User.builder()
                        .userName(userName)
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .roles(Collections.singletonList("ROLE_USER"))
                        .useAble(true)
                        .build();

                emailVerificationRepository.delete(emailVerification);
                userRepository.save(user);

                return ResultResponseDto.builder()
                        .code(1)
                        .msg("성공적으로 회원가입 하였습니다.")
                        .build();
            }else {
                return ResultResponseDto.builder()
                        .code(0)
                        .msg("회원가입에 실패하였습니다. 재발급 하세요.")
                        .build();
            }
        }

    @Override
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        log.info("[getSignInResult] signDataHandler 로 회원 정보 요청");
        User user = userRepository.getByEmail(signInRequestDto.getEmail());
        log.info("[getSignInResult] Id : {}", signInRequestDto.getEmail());

        log.info("[getSignInResult] 패스워드 비교 수행");
        if (!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException();
        }
        log.info("[getSignInResult] 패스워드 일치");
        log.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResponseDto signInResultDto = SignInResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(String.valueOf(user.getEmail()), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(String.valueOf(user.getEmail())))
                .email(user.getEmail())
                .userName(user.getUserName())
                .code(1)
                .msg("로그인에 성공하였습니다.")
                .build();
        log.info("[getSignInResult] SignInResultDto 객체에 값 주입");

        return signInResultDto;
    }

}
