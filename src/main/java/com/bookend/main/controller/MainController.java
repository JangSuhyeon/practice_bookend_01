package com.bookend.main.controller;

import com.bookend.security.dto.LoginUser;
import com.bookend.security.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String goToMain(@LoginUser SessionUser user, Model model) {
        if (user != null) {
            model.addAttribute("username", user.getName());
            System.out.println("username : " + user.getName());
        }
        return "index";
    }

}
