package com.bookStore.bookstore.module.client.DTO;

import java.util.UUID;

public record ClientResponseDTO(UUID id,
                                String username,
                                String email)  {
}
