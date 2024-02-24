package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final UtilsService utilsService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           TokenService tokenService,
                           UtilsService utilsService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.utilsService = utilsService;
    }

    @Override
    public String register(User newUser) {
        User user = userRepository.findByEmail(newUser.getEmail());
        if(user != null) {
            throw new EntityExistsException("User already exist");
        }
        newUser.setInitials(utilsService.generateInitials(newUser.getFullname()));
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userRepository.save(newUser);
        return "Registered successfully";
    }

    @Override
    public Map<String, String> forgotPassword(Authentication authentication, Map<String, String> forgotter) {
        if(!forgotter.containsKey("email")) {
            throw new BadCredentialsException("Bad Request: misses some fields");
        }
        User user = userRepository.findByEmail(forgotter.get("email"));
        if(user == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        Map<String, String> map = new HashMap<>();
        map.put("email", user.getEmail());
        map.put("token", tokenService.generateToken(authentication));
        return map;
    }

    @Override
    public String resetPassword(Map<String, String> resetModel) {
        if(!resetModel.containsKey("newPass")
                || !resetModel.containsKey("confirmPass")
            || !resetModel.containsKey("email")) {
            throw new BadCredentialsException("Bad request: missing fields");
        }
        if (!resetModel.get("newPass").equals(resetModel.get("confirmPass"))) {
            throw new BadCredentialsException("Passwords mismatch");
        }
        User user = userRepository.findByEmail(resetModel.get("email"));
        if(user == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        user.setPassword(passwordEncoder.encode(resetModel.get("newPass")));
        userRepository.save(user);
        return "Password reset successfully";
    }

    @Override
    public Map<String, String> login(Authentication authentication,
                                     Map<String, String> loginModel) {
        if(!loginModel.containsKey("email") || !loginModel.containsKey("password")) {
            throw new BadCredentialsException("Bad request");
        }
        User user = userRepository.findByEmail(loginModel.get("email"));
        if(user == null) {
            throw new EntityNotFoundException("User does not exist");
        }
        if(!passwordEncoder.matches(loginModel.get("password"), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        Map<String, String> map = new HashMap<>();
        map.put("fullname", user.getFullname());
        map.put("email", user.getEmail());
        map.put("initials", user.getInitials());
        map.put("token", tokenService.generateToken(authentication));
        return map;
    }
}
