package org.knulikelion.challengers_backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${springboot.jwt.secret}")
    private String secretkey;
    private final long tokenValidMillisecond = 1000L * 60 * 60; /*1시간 토큰 유효*/

    @PostConstruct
    protected void init(){
        log.info("[init] JWT TokenProvider 내 secretKey 초기화 시작");
        secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes(StandardCharsets.UTF_8));
        log.info("[init] JWT TokenProvider 내 secretKey 초기화 완료");
    }

    /*JWT Token Create*/

    /**
     * 토큰생성
     * @param userEmail
     * @param roles
     * @return token
     */
    public String createToken(String userEmail, List<String> roles){
        log.info("[createToken] 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userEmail); /*email 로 식별하는 Claims 객체 생성*/
        claims.put("roles", roles);

        Date now = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS384, secretkey) /*암호화 알고리즘, secret 값 세팅*/
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
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserInfo(token));
        log.info("[getAuthentication] 토큰 인증 정보 조회 완료 UserDetails : {}", userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserInfo(String token){
        log.info("[getUserInfo] 토큰 기반 회원 구별 정보 추출");
        String info = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
        log.info("[getUserInfo] 토큰 기반 회원 구별 정보 추출 완료, info : {}",info);
        return info;
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
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token);
            log.info("[validateToken] 토큰 유효 체크 완료");
            return !claimsJws.getBody().getExpiration().before(new Date()); /*JWT Token 만료 */
        }catch (Exception e){
            log.info("[validateToken] 토큰 유효 체크 예외 발생");
            return false;
        }
    }
}
