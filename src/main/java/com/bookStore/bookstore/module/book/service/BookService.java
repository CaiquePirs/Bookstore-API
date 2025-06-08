package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.book.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final BookValidator validator;
    private final BookMapper mapper;
    private final AuthorService authorService;

    public Book create(BookDTO dto){
        Author author = authorService.searchById(dto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.authorId()));

        validator.validateIsbn(dto);
        Book book = mapper.toEntity(dto);
        book.setAuthor(author);

        return repository.save(book);
    }

    public Optional<Book> searchById(UUID id){
        return repository.findById(id);
    }
}
