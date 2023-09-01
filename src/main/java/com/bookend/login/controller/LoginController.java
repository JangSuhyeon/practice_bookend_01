package com.bookend.login.controller;

import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    // 로그인 화면으로
    @GetMapping("/page")
    public String goToLogin(@LoginUser SessionUser user, Model model) {
        if (user != null) {
            System.out.println(user);
            model.addAttribute("userName", user.getName());
        }
        return "login/login";
    }
}
