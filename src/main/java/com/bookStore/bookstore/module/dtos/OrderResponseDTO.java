package com.bookStore.bookstore.module.dtos;

import com.bookStore.bookstore.module.enums.StatusOrder;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(UUID id,
                               String bookTitle,
                               String clientName,
                               String clientEmail,
                               LocalDateTime orderDate,
                               StatusOrder status) {
}
