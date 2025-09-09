package com.bookStore.bookstore.module.dtos;


import com.bookStore.bookstore.module.enums.StatusBook;

import java.time.LocalDate;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        String isbn,
        String publisher,
        StatusBook statusBook,
        LocalDate publicationDate,
        AuthorResponseDTO author) {
}
