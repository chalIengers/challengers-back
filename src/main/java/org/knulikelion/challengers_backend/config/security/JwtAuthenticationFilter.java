package org.knulikelion.challengers_backend.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final List<String> permitUrls = Arrays.asList("/api/v1/sign-in", "/api/v1/request-sign-up", "/api/v1/sign-up", "/api/v1/verify/account", "/api/v1/refresh-token",
            "/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception");


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return permitUrls.stream()
                .anyMatch(url -> new AntPathMatcher().match(url, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request); /*Token 생성*/
        log.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        if (token != null && jwtTokenProvider.validateUserAble(token) && jwtTokenProvider.validateToken(token)) {
            log.info("[doFilterInternal] token 값 유효성 체크 시작");
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[doFilterInternal] token 값 유효성 체크 완료");
        } else if (token != null) {
            log.error("[doFilterInternal] Token is not active");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User is not active");

            return;
        } else {
            log.warn("[doFilterInternal] Token is not valid");
        }
        log.info("[filterChain] filtering");
        filterChain.doFilter(request, response);
    }
}




