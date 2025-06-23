package com.pardalpizzaria.pizzaria.user.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pardalpizzaria.pizzaria.auth.dtos.request.RegisterUserDto;
import com.pardalpizzaria.pizzaria.user.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) @Size(max=50, message = "Name must be at most 50 characters long")
    private String name;

    @Column(nullable = false, unique = true) @Size(min=3, max=20, message = "Username must be between 3 and 20 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Username must be at least 3 characters long and can only contain letters, numbers, underscores, and hyphens")
    private String username;

    @Email
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;

    @Column(nullable = false)
    @Size(min=3, message = "Password must be at least 3 characters long")
    private String password;

    @Column(nullable = false)
    @Size(max=100, message = "Address must be at most 100 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9\\s,.'-]{1,100}$", 
             message = "Address must be at most 100 characters long and can only contain letters, numbers, spaces, commas, periods, apostrophes, and hyphens")
    private String address;

    @Column(name = "phone_number", nullable = false)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$",
             message = "Phone number must be between 10 and 15 digits long and can optionally start with a '+' sign")
    @Size(max=15, message = "Phone number must be at most 15 characters long")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable=false)
    @JsonIgnore
    private boolean isEnabled = false;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expiry")
    private LocalDateTime passwordResetTokenExpiry;

    @Column(name="createdAt", nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt", nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEnable(){
        return this.isEnabled;
    }

    public User(RegisterUserDto register, String password, Role role) {
        this.name = register.name();
        this.username = register.username();
        this.email = register.email();
        this.password = password;
        this.address = register.address();
        this.phoneNumber = register.phoneNumber();
        this.role = role;
    }

}
