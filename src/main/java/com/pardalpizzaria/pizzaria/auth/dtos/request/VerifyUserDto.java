package com.pardalpizzaria.pizzaria.auth.dtos.request;

public record VerifyUserDto(
    String email,
    String verificationCode
) {

}
