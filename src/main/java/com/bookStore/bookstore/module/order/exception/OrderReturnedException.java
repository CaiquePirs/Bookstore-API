package com.bookStore.bookstore.module.order.exception;

public class OrderReturnedException extends RuntimeException {
    public OrderReturnedException() {
        super("This order cannot be updated because it has already been returned");
    }
}
