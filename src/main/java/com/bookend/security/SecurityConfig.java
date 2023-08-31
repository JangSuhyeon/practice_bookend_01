package com.bookend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
//                .csrf(AbstractHttpConfigurer::disable) // Todo disable 안하면 어떻게 되는지 확인 필요
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests
                                .requestMatchers("/css/**", "/fonts/**", "/vendor/**","/images/**", "/js/**").permitAll() // 해당 url은 권한 없이 접근 가능
                                .anyRequest().authenticated())  // 이외 url은 인증된 사용자만 접근 가능
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/user/login").permitAll()) // 로그인 페이지 경로 지정
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/")) // 로그아웃 성공 시 이동할 url
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .userInfoEndpoint(userInfo ->   // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                                        userInfo.userService(customOAuth2UserService))); // oauth2 로그인 성공 이후 실행할 UserService 인터페이스의 구현체를 등록

        return http.build();
    }

}
