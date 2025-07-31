package com.exemple.auth.user.exception;

import com.exemple.auth.global.AppException;

public class UserNotFoundByUsername extends AppException {
    public UserNotFoundByUsername(String username) {
        super("User not found with username: " + username);
    }
}
