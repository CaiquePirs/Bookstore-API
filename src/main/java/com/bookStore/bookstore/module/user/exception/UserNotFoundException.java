package com.bookStore.bookstore.module.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("User ID not found: " + id);
    }
}