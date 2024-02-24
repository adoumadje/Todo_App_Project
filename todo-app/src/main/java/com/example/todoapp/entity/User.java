package com.example.todoapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "full_name")
    @NotBlank(message = "full name cannot be left empty")
    @Pattern(
            regexp = "^([A-Z][a-z]+)\\s+(?:([A-Z][a-z]+)\\s+)?([A-Z][a-z]+)$",
            message = "user must give his full name"
    )
    private String fullname;
    @Column(
            name = "email",
            unique = true
    )
    @NotBlank(message = "email cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email format invalid")
    private String email;
    @Column(name = "initials")
    private String initials;
    @Column(name = "password")
    @NotBlank(message = "password cannot be empty")
    @Size(min = 6, message = "Password must have at least 6 characters")
    private String password;

    public Long getId() {
        return id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullname='" + fullname + '\'' +
                ", initials='" + initials + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
