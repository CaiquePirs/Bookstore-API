package com.bookStore.bookstore.module.order.exception;


public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(String message) {
    super(message);
  }
}
