package com.example.todoapp.service;

import org.springframework.stereotype.Service;

@Service
public class UtilsService {
    public String generateInitials(String fullname) {
        String[] names = fullname.split(" ");
        return (names[0].charAt(0) + "" + names[1].charAt(0)).toUpperCase();
    }

    public String generateTodoColor(Integer status) {
        if(0 <= status && status < 25) {
            return "#ff3d32";
        } else if(25 <= status && status < 50) {
            return "#f97316";
        } else if(50 <= status && status < 75) {
            return "#14b8a6";
        } else if(75 <= status && status <= 100) {
            return "#22c55e";
        }
        return "";
    }
}
