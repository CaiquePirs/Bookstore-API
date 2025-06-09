package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.author.repository.BookSpecs;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.book.validator.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

        validator.validateIsbn(dto.isbn(), null);
        Book book = mapper.toEntity(dto);
        book.setAuthor(author);

        return repository.save(book);
    }

    public Optional<Book> getById(UUID id){
        return repository.findById(id);
    }

    public Page<Book> searchBooksByQuery(String title,
                                         String isbn,
                                         String publisher,
                                         String author,
                                         Integer page,
                                         Integer sizePage) {

        Specification<Book> specs = (root, query, cb) -> cb.conjunction(); // true

        if (isbn != null && !isbn.isBlank()) {
            specs = specs.and(BookSpecs.isbnEqual(isbn));
        }

        if (title != null && !title.isBlank()) {
            specs = specs.and(BookSpecs.titleLike(title));
        }

        if (publisher != null && !publisher.isBlank()) {
            specs = specs.and(BookSpecs.publisherEqual(publisher));
        }

        if (author != null && !author.isBlank()) {
            specs = specs.and(BookSpecs.authorNameLike(author));
        }

        Pageable pagerequest = PageRequest.of(page, sizePage);

        return repository.findAll(specs, pagerequest);
    }


    public void deleteById(UUID id){
        repository.deleteById(id);
    }

    public void searchISBN(Book book){
        validator.validateIsbn(book.getIsbn(), book.getId());
    }

    public Book update(Book book){
        return repository.save(book);
    }

}
