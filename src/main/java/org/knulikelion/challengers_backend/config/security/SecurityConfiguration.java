package org.knulikelion.challengers_backend.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
                        "/api/v1/request-sign-up",
                        "/api/v1/sign-up",
                        "/api/v1/verify/account",
                        "/api/v1/refresh-token").permitAll()

//                Project
                .antMatchers("/api/v1/project/get/all").permitAll()
                .antMatchers("/api/v1/project/get/all/top-viewed/{year}/{month}").permitAll()
                .antMatchers("/api/v1/project/get").permitAll()
                .antMatchers("/api/v1/project/create").hasRole("USER")
                .antMatchers("/api/v1/project/update").hasRole("USER")
                .antMatchers("/api/v1/project/remove").hasRole("USER")
                .antMatchers("/api/v1/project/tech-stacks").permitAll()

//                ProjectCrew
                .antMatchers("/api/v1/project-crews/**").permitAll()

//                File Upload
                .antMatchers("/api/v1/file/**").hasRole("USER")

//                User
                .antMatchers("/api/v1/user/get").hasRole("USER")
                .antMatchers("/api/v1/user/remove").hasRole("USER")
                .antMatchers("/api/v1/user/update").hasRole("USER")

//                MyPage
                .antMatchers("/api/v1/mypage/**").hasRole("USER")

//                Club
                .antMatchers("/api/v1/club/get/logo/all").permitAll()
                .antMatchers("/api/v1/club/list").permitAll()
                .antMatchers("/api/v1/club/get").permitAll()
                .antMatchers("/api/v1/members/{clubId}").hasRole("USER")
                .antMatchers("/api/v1/club/create").hasRole("USER")
                .antMatchers("/api/v1/club/update").hasRole("USER")
                .antMatchers("/api/v1/club/remove").hasRole("USER")
                .antMatchers("/api/v1/club/addMember").hasRole("USER")
                .antMatchers("/api/v1/club/updateMember").hasRole("USER")
                .antMatchers("/api/v1/club/deleteMember").hasRole("USER")
                .antMatchers("/api/v1/club/join-requests/pending/users/{clubId}").hasRole("USER")
                .antMatchers("/api/v1/club/join-requests/accept/{clubId}").hasRole("USER")
                .antMatchers("/api/v1/club/join-requests/reject/{clubId}").hasRole("USER")
                .antMatchers("/api/v1/club/join-requests/comment/{requestId}").hasRole("USER")
                .antMatchers("/api/v1/club/verify-club").hasRole("USER")

//                Admin
                .antMatchers("/api/v1/admin/sign").permitAll()
                .antMatchers("/api/v1/admin/set").permitAll()
                .antMatchers("/api/v1/admin/**").hasRole("ADMIN")


                .anyRequest().permitAll()
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
