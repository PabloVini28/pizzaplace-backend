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
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
    @Pattern(
        regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s'-]{2,50}$",
        message = "O nome deve conter apenas letras, espaços, apóstrofos ou hífens."
    )
    private String name;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 20, message = "O nome de usuário deve ter entre 3 e 20 caracteres.")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]{3,}$",
        message = "O nome de usuário pode conter letras, números, pontos, underscores ou hífens."
    )
    private String username;

    @Email(message = "Formato de email inválido.")
    @Column(nullable = false, unique = true)
    @Pattern(
        regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
        message = "Formato de email inválido."
    )
    private String email;

    @Column(nullable = false)
    @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres.")
    private String password;

    @Column(nullable = false)
    @Size(max = 100, message = "O endereço deve ter no máximo 100 caracteres.")
    private String address;

    @Column(name = "phone_number", nullable = false)
    @Pattern(
        regexp = "^\\+?[0-9]{10,15}$",
        message = "O telefone deve ter entre 10 e 15 dígitos e pode começar com '+'."
    )
    @Size(max = 15, message = "O telefone deve ter no máximo 15 caracteres.")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
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

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isEnable() {
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
