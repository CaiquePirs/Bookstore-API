package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.DTO.ResponseBookDTO;
import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements GenericController {

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookDTO bookDTO){
          Book book = service.create(bookDTO);

          var uri = generateHeaderLocation(book.getId());
          return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseBookDTO> searchBook(@PathVariable UUID id){
        return service.searchById(id)
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);

        }).orElseThrow(() -> new BookNotFoundException(id));
    }
}
