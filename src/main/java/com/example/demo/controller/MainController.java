package com.example.demo.controller;

import com.example.demo.dto.UserRequestDto;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.security.CurrentUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    @GetMapping("/login/page")
    public String loginPage() {
        return "login";
    }


    @GetMapping("/register")
    public String registerPage(ModelMap modelMap) {
        modelMap.addAttribute("userRequest", new UserRequestDto());
        return "register";
    }

    @GetMapping("/success/login")
    public String successLogin(@AuthenticationPrincipal CurrentUser currentUser) {
        if (currentUser == null) {
            return "redirect:/";
        }
        User user = currentUser.getUser();
        if (user.getRole() == Role.TEAM_LEADER) {
            return "redirect:/leader/page";
        } else {
            return "redirect:/member/page";
        }
    }
}
