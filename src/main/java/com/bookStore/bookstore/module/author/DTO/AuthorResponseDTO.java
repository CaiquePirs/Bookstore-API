package com.bookStore.bookstore.module.author.DTO;

import com.bookStore.bookstore.module.author.model.StatusAuthor;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID id,
        String name,
        String nationality,
        String biography,
        StatusAuthor status,
        LocalDate dateBirth) {
}
