package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.book.model.Author;
import com.bookStore.bookstore.module.book.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository repository;

    public Author create(Author author){
        return repository.save(author);
    }

}
