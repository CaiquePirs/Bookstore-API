package com.bookStore.bookstore.module.book.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record BookDTO(
        UUID id,
        @NotBlank(message = "The book title is required")
        String title,

        @NotBlank(message = "The book ISBN is required")
        //@ISBN(message = "Enter a valid ISBN")
        String isbn,

        @NotBlank(message = "The publisher book is required")
        @Size(max = 50, message = "the publisher must be up to 50 characters")
        String publisher,

        @NotNull(message = "The book publication date is required")
        LocalDate publicationDate,

        @NotNull(message = "Author ID is required")
        UUID authorId) {
}
