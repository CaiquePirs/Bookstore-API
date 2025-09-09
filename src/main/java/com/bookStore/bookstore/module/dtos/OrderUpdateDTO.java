package com.bookStore.bookstore.module.dtos;

import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.enums.StatusOrder;
import java.util.UUID;

public record OrderUpdateDTO(
        UUID bookId,
        UUID clientId,
        StatusOrder statusOrder,
        StatusBook statusBook) {
}
