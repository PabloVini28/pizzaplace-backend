package com.pardalpizzaria.pizzaria.auth.dtos.request;

public record RegisterUserDto(
    String name,
    String username,
    String email,
    String password,
    String confirmPassword,
    String address,
    String phoneNumber
) {
    
}
