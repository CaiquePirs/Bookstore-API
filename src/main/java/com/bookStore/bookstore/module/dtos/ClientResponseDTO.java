package com.bookStore.bookstore.module.dtos;

import java.util.UUID;

public record ClientResponseDTO(UUID id,
                                String username,
                                String email)  {
}
