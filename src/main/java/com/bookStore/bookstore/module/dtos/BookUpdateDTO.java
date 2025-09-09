package com.bookStore.bookstore.module.dtos;

import java.time.LocalDate;
import java.util.UUID;

public record BookUpdateDTO(
        String title,
        String isbn,
        String publisher,
        LocalDate publicationDate,
        UUID authorId) {
}
