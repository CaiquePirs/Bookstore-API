package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.book.model.Author;
import com.bookStore.bookstore.module.book.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository repository;

    public Author create(Author author){
        return repository.save(author);
    }

    public Optional<Author> search(UUID id){
        return repository.findById(id);
    }

    public void delete(Author author){
        repository.delete(author);
    }

}
