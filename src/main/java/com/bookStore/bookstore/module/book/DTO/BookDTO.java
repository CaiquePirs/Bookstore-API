package com.bookStore.bookstore.module.book.DTO;

import java.time.LocalDate;
import java.util.UUID;

public record BookDTO(String title, String isbn, String publisher, LocalDate publicationDate, UUID author_id) {

}
