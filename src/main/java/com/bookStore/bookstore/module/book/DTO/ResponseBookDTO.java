package com.bookStore.bookstore.module.book.DTO;

import com.bookStore.bookstore.module.author.DTO.AuthorResponseDTO;
import com.bookStore.bookstore.module.book.model.StatusBook;

import java.time.LocalDate;
import java.util.UUID;

public record ResponseBookDTO(
        UUID id,
        String title,
        String isbn,
        String publisher,
        StatusBook status,
        LocalDate publicationDate,
        AuthorResponseDTO author) {
}
