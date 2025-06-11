package com.bookStore.bookstore.module.order.DTO;

import com.bookStore.bookstore.module.order.model.StatusOrder;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(UUID id,
                               String bookTitle,
                               String userName,
                               String userEmail,
                               LocalDateTime dateOrder,
                               StatusOrder statusOrder) {
}
