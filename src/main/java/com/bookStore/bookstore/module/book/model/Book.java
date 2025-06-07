package com.bookStore.bookstore.module.book.model;


import com.bookStore.bookstore.module.author.model.Author;
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
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false, length = 50)
    private String publisher;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn(name = "authorId")
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
