package com.bookend.login.controller;

import com.bookend.security.CustomOAuth2UserService;
import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/login")
public class LoginController {

    private final CustomOAuth2UserService customOAuth2UserService;

    // 로그인 화면으로
    @GetMapping("/page")
    public String goToLogin(@LoginUser SessionUser user, Model model) {
        System.out.println("로그인페이지");
        return "login/login";
    }

    // 게스트 로그인
    @GetMapping("/guest")
    public String guestLogin() {
        customOAuth2UserService.guestLogin(); // 게스트 계정 생성

        return "redirect:/";
    }
}
