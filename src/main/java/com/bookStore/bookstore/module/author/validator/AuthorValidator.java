package com.bookStore.bookstore.module.author.validator;

import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository repository;

    public void validate(Author author){
        if(existsDuplicateAuthor(author)){
            throw new DuplicateRecordException("Author already registered");
        }
    }

    private boolean existsDuplicateAuthor(Author author) {
        Optional<Author> authorFound = repository.findAuthorByName(author.getName());
        return authorFound.isPresent() &&
                !authorFound.get().getId().equals(author.getId());
    }
}
