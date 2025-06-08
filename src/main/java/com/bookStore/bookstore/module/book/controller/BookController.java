package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.author.service.AuthorService;
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
    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookDTO bookDTO){
          Book book = service.create(bookDTO);
          var uri = generateHeaderLocation(book.getId());
          return ResponseEntity.created(uri).body(mapper.toDTO(book));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseBookDTO> searchBook(@PathVariable UUID id){
        return service.getById(id)
                .map(book -> {
                    var dto = mapper.toDTO(book);
                    return ResponseEntity.ok(dto);
        }).orElseThrow(() -> new BookNotFoundException(id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteById(@PathVariable UUID id){
        return service.getById(id).
                map(book -> {
                    service.deleteById(id);
                    return ResponseEntity.noContent().build();
        }).orElseThrow(() -> new BookNotFoundException(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseBookDTO> update(@PathVariable UUID id, @RequestBody BookDTO dto){
        return service.getById(id).map(book -> {
            service.searchISBN(book);

            var author = authorService.searchById(dto.authorId())
                    .orElseThrow(() -> new AuthorNotFoundException(dto.authorId()));

            book.setIsbn(dto.isbn());
            book.setTitle(dto.title());
            book.setPublisher(dto.publisher());
            book.setPublicationDate(dto.publicationDate());
            book.setAuthor(author);

            var updatedBook = service.update(book);
            return ResponseEntity.ok(mapper.toDTO(updatedBook));

        }).orElseThrow(() -> new BookNotFoundException(id));


    }

}
