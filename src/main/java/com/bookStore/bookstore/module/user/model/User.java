package com.bookStore.bookstore.module.user.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30, nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate dateBirth;

    @CreationTimestamp
    private LocalDateTime CreationTimestamp;

    @UpdateTimestamp
    private LocalDateTime UpdateTimestamp;

    public User(){}

    public User(String username, String email, String password, LocalDate dateBirth){
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateBirth = dateBirth;
    }
}
