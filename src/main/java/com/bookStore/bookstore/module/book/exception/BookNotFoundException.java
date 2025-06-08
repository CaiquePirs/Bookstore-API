package com.bookStore.bookstore.module.book.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(UUID id) {
            super("Book ID not found: " + id);
        }
}
