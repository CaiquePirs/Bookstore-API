package com.bookStore.bookstore.module.validators;

import com.bookStore.bookstore.module.dtos.BookUpdateDTO;
import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.repositories.BookRepository;
import com.bookStore.bookstore.module.common.exceptions.DuplicateRecordException;
import com.bookStore.bookstore.module.services.AuthorService;
import com.bookStore.bookstore.module.services.ClientAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final ClientAuditService auditService;

    public void validateIsbn(String isbn, UUID currentBookId) {
        repository.findByIsbn(isbn).ifPresent(existingBook -> {
            if (!existingBook.getId().equals(currentBookId)) {
                throw new DuplicateRecordException("Book ISBN already registered");
            }
        });
    }

    public Book updateValidator(Book book, BookUpdateDTO dto){
        if(dto.title() != null) book.setTitle(dto.title());
        if(dto.publisher() != null) book.setPublisher(dto.publisher());
        if(dto.publicationDate() != null) book.setPublicationDate(dto.publicationDate());

        if(dto.isbn() != null) {
            validateIsbn(dto.isbn(), book.getId());
            book.setIsbn(dto.isbn());
        }

        if(dto.authorId() != null){
            Author author = authorService.findById(dto.authorId());
            book.setAuthor(author);
        }
        book.setUserAuditId(auditService.getCurrentUserAuditId());
        return book;
    }

}
