package com.bookStore.bookstore.module.author.repository;

import com.bookStore.bookstore.module.author.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
    Optional<Author> findAuthorByName(String name);
}
