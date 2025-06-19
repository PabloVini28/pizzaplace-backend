package com.pardalpizzaria.pizzaria.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pardalpizzaria.pizzaria.user.dtos.response.UserResponseDto;
import com.pardalpizzaria.pizzaria.user.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(user))
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(UserResponseDto::new)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDto::new)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

}
