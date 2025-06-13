package com.bookStore.bookstore.module.author.repository;

import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.model.StatusAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID>, JpaSpecificationExecutor<Author> {
    Optional<Author> findByNameAndStatus(String name, StatusAuthor status);
}
