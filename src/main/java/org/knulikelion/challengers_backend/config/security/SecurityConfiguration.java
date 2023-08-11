package org.knulikelion.challengers_backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 웹 보안 설정 정의
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity)throws Exception{
        httpSecurity.httpBasic().disable() /*기본 http 인증 기능 비활성화, JWT 인증 사용*/
                .csrf().disable() /*CSRF 공격을 막기 위한 보호기능 비활성화*/
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) /*웹에서 세션 사용하지 않도록, JWT를 사용하여 상태 저장*/
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/sign-in",
                        "/api/v1/sign-up",
                        "/api/v1/exception").permitAll()
                .antMatchers("**exception**").permitAll()

                .anyRequest().hasRole("ADMIN") /*나머지 요청은 인증된 ADMIN만 접근 가능*/
                .and()
                .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class); /*UsernamePasswordAuthenticationFilter 앞에 JwtAuthenticationFilter 추가*/
    }

    @Override
    public void configure(WebSecurity webSecurity){
        webSecurity.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception");
    }
}
