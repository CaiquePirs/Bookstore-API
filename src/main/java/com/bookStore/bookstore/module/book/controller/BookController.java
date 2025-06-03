package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService service;

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody BookDTO bookDTO){
       var book = service.create(bookDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(book.getID())
                .toUri();

        return ResponseEntity.created(uri).body(book);
    }
}
