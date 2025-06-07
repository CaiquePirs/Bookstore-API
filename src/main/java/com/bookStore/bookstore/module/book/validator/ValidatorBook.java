package com.bookStore.bookstore.module.book.validator;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidatorBook {

    @Autowired
    private BookRepository repository;

    public void validateIsbn(BookDTO dto){
        Optional<Book> isbnFound = repository.findByIsbn(dto.isbn());

        if(isbnFound.isPresent()){
            throw new DuplicateRecordException("Book ISBN already registered");
        }
    }
}
