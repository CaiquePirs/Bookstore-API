package com.bookStore.bookstore.module.common.exceptions;


public class OrderLoanedException extends RuntimeException {
    public OrderLoanedException(String message) {
        super(message);
    }
}
