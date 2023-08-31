package com.bookend.user.controller;

import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String goToLogin(@LoginUser SessionUser user, Model model) {
        if (user != null) {
            System.out.println(user.toString());
            model.addAttribute("userName", user.getName());
        }
        return "user/login";
    }

}
