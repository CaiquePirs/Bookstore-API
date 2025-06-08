package com.bookStore.bookstore.module.author.DTO;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID id,
        String name,
        String nationality,
        String biography,
        LocalDate dateBirth) {
}
