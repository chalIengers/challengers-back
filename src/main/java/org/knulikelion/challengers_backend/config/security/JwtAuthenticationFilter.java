package org.knulikelion.challengers_backend.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request); /*Token 생성*/
        log.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        log.info("[doFilterInternal] token 값 유효성 체크 시작");
        if (token!=null & jwtTokenProvider.validateToken(token)) {
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("[doFilterInternal] token 값 유효성 체크 완료");
            }catch (RuntimeException e){
                log.error("[doFilterInternal] User is not active");
            }
        }

        filterChain.doFilter(request, response);
    }
}
