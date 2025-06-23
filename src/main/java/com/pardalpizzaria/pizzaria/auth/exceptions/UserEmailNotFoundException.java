package com.pardalpizzaria.pizzaria.auth.exceptions;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String email) {
        super("User with email " + email + " not found.");
    }

}
