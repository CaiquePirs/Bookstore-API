package com.bookStore.bookstore.module.dtos;

import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.enums.StatusOrder;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderRequestDTO(@NotNull(message = "Book ID is required")
                              UUID bookId,

                              @NotNull(message = "Client ID is required")
                              UUID clientId,

                              StatusOrder statusOrder,
                              StatusBook statusBook){
}
