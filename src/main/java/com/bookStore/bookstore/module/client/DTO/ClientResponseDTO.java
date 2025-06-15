package com.bookStore.bookstore.module.client.DTO;


import java.time.LocalDate;
import java.util.UUID;

public record ClientResponseDTO(UUID id,
                                String username,
                                String email,
                                String password,
                                LocalDate dateBirth)  {
}
