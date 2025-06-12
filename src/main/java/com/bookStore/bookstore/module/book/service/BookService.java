package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.repository.BookSpecs;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.book.validator.BookValidator;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final OrderRepository orderRepository;
    private final BookValidator validator;
    private final BookMapper mapper;
    private final AuthorService authorService;

    public Book create(BookDTO dto){
        Author author = authorService.searchById(dto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.authorId()));

        validator.validateIsbn(dto.isbn(), null);
        Book book = mapper.toEntity(dto);
        book.setAuthor(author);
        book.setStatus(StatusBook.AVAILABLE);
        return repository.save(book);
    }

    public Book getById(UUID id) {
        return repository.findById(id)
                .map((Book book) -> {
                    if (book.getStatus().equals(StatusBook.DELETED_AT)) {
                        throw new BookUnavailableException("This book is deleted in the system");
                    }
                    return book;
                })
                .orElseThrow(() -> new BookNotFoundException(id));
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
