package com.bookStore.bookstore.module.book.repository;

import com.bookStore.bookstore.module.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
}
