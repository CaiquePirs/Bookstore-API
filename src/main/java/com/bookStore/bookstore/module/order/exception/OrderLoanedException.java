package com.bookStore.bookstore.module.order.exception;


public class OrderLoanedException extends RuntimeException {
    public OrderLoanedException(String message) {
        super(message);
    }
}
