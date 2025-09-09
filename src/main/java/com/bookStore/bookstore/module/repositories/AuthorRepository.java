package com.bookStore.bookstore.module.repositories;

import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.enums.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID>, JpaSpecificationExecutor<Author> {
    Optional<Author> findByNameAndStatus(String name, StatusEntity status);
}
