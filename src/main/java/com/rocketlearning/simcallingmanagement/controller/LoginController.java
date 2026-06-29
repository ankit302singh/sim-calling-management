package com.rocketlearning.simcallingmanagement.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.rocketlearning.simcallingmanagement.dto.LoginRequest;
import com.rocketlearning.simcallingmanagement.entity.Role;
import com.rocketlearning.simcallingmanagement.entity.User;
import com.rocketlearning.simcallingmanagement.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {

        return "login";

    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest,
                        HttpSession session) {

        User user = userService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword());

        if (user == null) {

            return "redirect:/login";

        }

        // Save logged-in user in session
        session.setAttribute("loggedInUser", user);

        // Redirect based on role
        if (user.getRole() == Role.ADMIN) {

            return "redirect:/dashboard";

        } else {

            return "redirect:/employee/dashboard";

        }

    }

}