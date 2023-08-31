package org.knulikelion.challengers_backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.knulikelion.challengers_backend.data.entity.User;
import org.knulikelion.challengers_backend.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Value("${springboot.jwt.secret}")
    private String secretKey;
    private final long accessTokenValidMillisecond = 1000L * 60 * 60; /*1시간 토큰 유효*/
    private final long refreshTokenValidMillisecond = 1000L * 60 * 60 * 24 * 14; /*2주 토큰 유효*/

    /*JWT Token Create*/

    /**
     * 토큰생성
     * @param userEmail
     * @param roles
     * @return token
     */
    public String createAccessToken(String userEmail, List<String> roles) {
        return createToken(userEmail, roles, accessTokenValidMillisecond);
    }

    public String createRefreshToken(String userEmail) {
        return createToken(userEmail, new ArrayList<>(), refreshTokenValidMillisecond);
    }

    private String createToken(String userEmail, List<String> roles, long validMillisecond) {
        log.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        log.info("[createToken] 토큰 생성 완료");
        return token;
    }

    /**
     * 필터에서 인증이 성공했을 때, SecurityContextHolder 에 저장할 Authentication 생성하는 역할
     * @param token
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token){ /*JWT Token 으로 인증 정보 조회*/
        log.info("[getAuthentication] 토큰 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료 UserDetails : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserEmail(String token){
        log.info("[getUserInfo] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getUserInfo] 토큰 기반 회원 구별 정보 추출 완료, info : {}",info);
        return info;
    }

    public String refreshToken(String refreshToken, String userEmail) {
        log.info("[refreshToken] 리프레시 토큰으로 액세스 토큰 재발급 시작");

        // 리프레시 토큰 유효성 검사
        if (validateToken(refreshToken)) {
            // 유저의 이메일로 새로운 액세스 토큰 생성
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            return createAccessToken(userEmail, roles);
        } else {
            log.error("[refreshToken] 리프레시 토큰이 유효하지 않음");
            throw new RuntimeException("Refresh token is not valid");
        }
    }

    public boolean validateUserAble(String token){
        User user = userRepository.getByEmail(getUserEmail(token));
        System.out.println("result : "+user.isUseAble());
        return user.isUseAble();
    }


    /**
     * HTTP Request Header 에 설정된 토큰 값을 가져옴
     *
     * @param request Http Request Header
     * @return String type Token 값
     */
    public String resolveToken(HttpServletRequest request){
        log.info("[resolveToken] HTTP 헤더에서 Token 값 추출");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token){ /*JWT 토큰의 유효성 + 만료일 체크*/
        log.info("[validateToken] 토큰 유효 체크 시작");

        if(!validateUserAble(token)){
            log.info("[validateToken] 비활성화된 Token");
            return false;
        }

        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info("[validateToken] 토큰 유효 체크 완료");
            return !claimsJws.getBody().getExpiration().before(new Date()); /*JWT Token 만료 */
        }catch (Exception e){
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
