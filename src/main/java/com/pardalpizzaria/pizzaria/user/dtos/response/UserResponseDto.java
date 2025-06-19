package com.pardalpizzaria.pizzaria.user.dtos.response;

import com.pardalpizzaria.pizzaria.user.entity.User;

public record UserResponseDto(
    Long id,
    String name,
    String username,
    String email,
    String address
) {

    public UserResponseDto(User user) {
        this(
            user.getId(),
            user.getName(),
            user.getUsername(),
            user.getEmail(),
            user.getAddress()
        );
    }
    
}
