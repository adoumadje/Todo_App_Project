package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import org.springframework.security.core.Authentication;

import java.util.Map;

public interface AuthService {
    String register(User newUser);

    Map<String, String> forgotPassword(Authentication authentication,
                                       Map<String, String> forgotter);

    String resetPassword(Map<String, String> resetModel);

    Map<String, String> login(Authentication authentication,
                              Map<String, String> loginModel);
}
