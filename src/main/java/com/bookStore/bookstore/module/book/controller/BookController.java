package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.common.error.ErrorResponse;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import jakarta.validation.Valid;
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
    private BookService service;

    @Autowired
    private BookMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookDTO bookDTO){
       try {
          Book book = service.create(bookDTO);

           URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                   .path("/{id}")
                   .buildAndExpand(book.getId())
                   .toUri();

           return ResponseEntity.created(uri).body(mapper.toDTO(book));

       } catch (DuplicateRecordException e) {
        var errorDTO = ErrorResponse.conflict(e.getMessage());
        return ResponseEntity.status(errorDTO.status()).body(errorDTO);
    }
    }
}
