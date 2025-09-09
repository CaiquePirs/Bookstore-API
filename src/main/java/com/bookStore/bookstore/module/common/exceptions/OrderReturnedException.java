package com.bookStore.bookstore.module.common.exceptions;

public class OrderReturnedException extends RuntimeException {
    public OrderReturnedException(String message) {
        super(message);
    }
}
