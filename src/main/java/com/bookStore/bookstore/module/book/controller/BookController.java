package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.docs.controllers.BookControllerDoc;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.DTO.ResponseBookDTO;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements GenericController, BookControllerDoc {

    private final BookService service;
    private final BookMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBookDTO> create(@RequestBody @Valid BookDTO bookDTO){
          Book book = service.create(bookDTO);
          var uri = generateHeaderLocation(book.getId());
          return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBookDTO> searchBookById(@PathVariable UUID id){
        var book = service.getById(id);
        return ResponseEntity.ok(mapper.toDTO(book));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<ResponseBookDTO>> searchBookByQuery(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "status", required = false) StatusBook status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage){

        Page<Book> pageResult = service.searchBooksByQuery(title, isbn, publisher, author, status, page, sizePage);
        Page<ResponseBookDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseBookDTO> update(@PathVariable UUID id, @RequestBody BookDTO dto){
        var book = service.update(id, dto);
        var uri = generateHeaderLocation(id);
        return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

}
