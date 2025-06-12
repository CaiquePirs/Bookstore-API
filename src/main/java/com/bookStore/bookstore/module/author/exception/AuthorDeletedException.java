package com.bookStore.bookstore.module.author.exception;

public class AuthorDeletedException extends RuntimeException {
    public AuthorDeletedException(String message) {
        super(message);
    }
}
