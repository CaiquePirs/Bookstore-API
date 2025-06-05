    package com.bookStore.bookstore.module.author.model;

import com.bookStore.bookstore.module.book.model.Book;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nationality;

    @Column(length = 100, nullable = false)
    private String biography;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @UpdateTimestamp
    private LocalDateTime update;

    public Author(){}

    public Author(String name, String nationality, String biography, LocalDate dateBirth){
        this.name = name;
        this.nationality = nationality;
        this.biography = biography;
        this.dateBirth = dateBirth;
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();

}
