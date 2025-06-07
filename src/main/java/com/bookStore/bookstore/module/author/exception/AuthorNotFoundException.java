package com.bookStore.bookstore.module.author.exception;

import java.util.UUID;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(UUID id) {
        super("Author ID not found: " + id);
    }
}
