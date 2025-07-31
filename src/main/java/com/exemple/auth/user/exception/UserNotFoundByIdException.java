package com.exemple.auth.user.exception;

import com.exemple.auth.global.AppException;

public class UserNotFoundByIdException extends AppException {
    public UserNotFoundByIdException (Long id) {
        super("User not found with id: " + id);
    }
}