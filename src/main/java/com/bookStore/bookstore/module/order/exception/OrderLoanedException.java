package com.bookStore.bookstore.module.order.exception;


public class OrderLoanedException extends RuntimeException {
    public OrderLoanedException() {
        super("Error deleting: This order is active");
    }
}
