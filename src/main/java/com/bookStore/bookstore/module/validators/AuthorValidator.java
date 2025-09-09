package com.bookStore.bookstore.module.validators;

import com.bookStore.bookstore.module.common.exceptions.DuplicateRecordException;
import com.bookStore.bookstore.module.dtos.AuthorRequestDTO;
import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository repository;

    public void existsDuplicateAuthor(Author author){
        Optional<Author> authorExist = repository.findByNameAndStatus(author.getName(), StatusEntity.ACTIVE);
        boolean existsDuplicateAuthor = authorExist.isPresent() && !authorExist.get().getId().equals(author.getId());
        if(existsDuplicateAuthor) throw new DuplicateRecordException("Author already registered");
    }

    public Author updateValidator(Author author, AuthorRequestDTO dto){
        if (dto.name() != null) author.setName(dto.name());
        if (dto.nationality() != null) author.setNationality(dto.nationality());
        if (dto.biography() != null) author.setBiography(dto.biography());
        if (dto.dateBirth() != null) author.setDateBirth(dto.dateBirth());
        return author;
    }

}
