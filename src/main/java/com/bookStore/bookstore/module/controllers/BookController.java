package com.bookStore.bookstore.module.controllers;

import com.bookStore.bookstore.docs.BookApi;
import com.bookStore.bookstore.module.dtos.BookRequestDTO;
import com.bookStore.bookstore.module.dtos.BookResponseDTO;
import com.bookStore.bookstore.module.dtos.BookUpdateDTO;
import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.mappers.BookMapper;
import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.services.BookService;
import com.bookStore.bookstore.module.utils.UtilsMethods;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final BookService service;
    private final BookMapper mapper;
    private final UtilsMethods generic;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody @Valid BookRequestDTO bookRequestDTO){
          Book book = service.create(bookRequestDTO);
          URI uri = generic.generateHeaderLocation(book.getId());
          return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id){
        Book book = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(book));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Page<BookResponseDTO>> searchBookByQuery(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "status", required = false) StatusBook status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage){

        Page<Book> pageResult = service.filterByQuery(title, isbn, publisher, author, status, page, sizePage);
        Page<BookResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id){
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponseDTO> update(@PathVariable UUID id, @RequestBody BookUpdateDTO dto){
        Book book = service.update(id, dto);
        URI uri = generic.generateHeaderLocation(id);
        return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

}
