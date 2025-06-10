package com.bookStore.bookstore.module.order.exception;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(UUID id) {
    super("Order ID not found: " + id);
  }
}
