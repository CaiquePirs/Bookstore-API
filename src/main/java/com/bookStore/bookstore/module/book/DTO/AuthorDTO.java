package com.bookStore.bookstore.module.book.DTO;

import com.bookStore.bookstore.module.book.model.Author;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorDTO(UUID id, String name, String nacionality, String biography, LocalDate dateBirth) {

    public Author author(){
        Author author = new Author(this.name, this.nacionality, this.biography, this.dateBirth);
        return author;
    }
}
