package com.bookStore.bookstore.module.services;

import com.bookStore.bookstore.module.common.exceptions.NotFoundException;
import com.bookStore.bookstore.module.dtos.BookUpdateDTO;
import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.specifications.BookSpecs;
import com.bookStore.bookstore.module.dtos.BookRequestDTO;
import com.bookStore.bookstore.module.mappers.BookMapper;
import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.repositories.BookRepository;
import com.bookStore.bookstore.module.validators.BookValidator;
import com.bookStore.bookstore.module.validators.OrderValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final OrderValidator orderValidator;
    private final BookValidator validator;
    private final BookMapper mapper;
    private final AuthorService authorService;
    private final ClientAuditService clientAuditService;

    public Book create(BookRequestDTO dto){
        Author author = authorService.findById(dto.authorId());
        validator.validateIsbn(dto.isbn(), null);
        Book book = mapper.toEntity(dto);

        book.setAuthor(author);
        book.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        book.setStatus(StatusBook.AVAILABLE);
        return repository.save(book);
    }

    public Book findById(UUID id) {
        return repository.findById(id)
                .filter(b -> !b.getStatus().equals(StatusBook.DELETED_AT))
                .orElseThrow(() -> new NotFoundException("Book ID not found"));
    }

    public Page<Book> filterByQuery(String title, String isbn, String publisher,
                                    String author, StatusBook status, Integer page, Integer sizePage) {
        return repository.findAll(
                BookSpecs.specification(title, isbn, publisher, author, status),
                PageRequest.of(page, sizePage)
        );
    }

    public void deleteById(UUID id){
        Book book = findById(id);

        orderValidator.validateIfExistOrderActive(book);

        book.setStatus(StatusBook.DELETED_AT);
        book.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        repository.save(book);
    }

    public Book update(UUID bookId, BookUpdateDTO dto){
        Book book = findById(bookId);
        book = validator.updateValidator(book, dto);
        return repository.save(book);
    }

}
