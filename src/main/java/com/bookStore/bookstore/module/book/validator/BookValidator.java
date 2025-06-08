package com.bookStore.bookstore.module.book.validator;

import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BookValidator {
    private final BookRepository repository;

    public void validateIsbn(String isbn, UUID currentBookId) {
        repository.findByIsbn(isbn).ifPresent(existingBook -> {
            if (!existingBook.getId().equals(currentBookId)) {
                throw new DuplicateRecordException("Book ISBN already registered");
            }
        });
    }

}
