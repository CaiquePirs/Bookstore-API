package com.bookStore.bookstore.module.order.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(UUID id,
                               String bookTitle,
                               String userName,
                               String userEmail,
                               LocalDateTime dateOrder,
                               String statusOrder) {
}
