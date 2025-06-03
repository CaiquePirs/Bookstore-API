package com.bookStore.bookstore.module.book.repository;

import com.bookStore.bookstore.module.book.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {
}
