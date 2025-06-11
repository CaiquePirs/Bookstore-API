package com.bookStore.bookstore.module.order.exception;

public class OrderReturnedException extends RuntimeException {
    public OrderReturnedException() {
        super("This order has already been returned");
    }
}
