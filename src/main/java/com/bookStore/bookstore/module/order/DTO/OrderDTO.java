package com.bookStore.bookstore.module.order.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderDTO(@NotNull(message = "Book ID is required")
                       UUID bookId,
                       @NotNull(message = "User ID is required")
                       UUID userId) {
}
