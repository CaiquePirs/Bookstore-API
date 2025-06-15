package com.bookStore.bookstore.module.order.DTO;

import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderDTO(UUID id,
                       @NotNull(message = "Book ID is required")
                       UUID bookId,
                       @NotNull(message = "Client ID is required")
                       UUID clientId,
                       StatusOrder statusOrder,
                       StatusBook statusBook){
}
