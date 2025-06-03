package com.bookStore.bookstore.module.book.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ID;

    private String title;
    private String isbn;
    private String publisher;
    private LocalDate publicationDate;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    public Book(){}

    public Book(String title, String isbn, String publisher, LocalDate publicationDate, Author author){
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.author = author;
    }
}
