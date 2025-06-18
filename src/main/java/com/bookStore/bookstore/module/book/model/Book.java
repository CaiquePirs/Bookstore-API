package com.bookStore.bookstore.module.book.model;

import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.order.model.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "books")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBook status;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonBackReference
    private Author author;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @CreationTimestamp
    private LocalDateTime creationDate;

    @UpdateTimestamp
    private LocalDateTime UpdateTimestamp;

    private UUID userAuditId;

    @OneToMany(mappedBy = "book")
    private List<Order> orders = new ArrayList<>();

    public Book(){}

}
