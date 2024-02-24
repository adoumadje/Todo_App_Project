package com.example.todoapp.controller;

import com.example.todoapp.entity.User;
import com.example.todoapp.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User newUser) {
        return authService.register(newUser);
    }

    @PostMapping("/forgot-password")
    public Map<String, String> forgotPassword(
            Authentication authentication,
            @RequestBody Map<String, String> forgotter
    ) {
        return authService.forgotPassword(authentication, forgotter);
    }

    @PutMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> resetModel) {
        return authService.resetPassword(resetModel);
    }

    @PostMapping("/login")
    public Map<String, String> login(Authentication authentication,
                                     @RequestBody Map<String, String> loginModel) {
        return authService.login(authentication, loginModel);
    }
}
