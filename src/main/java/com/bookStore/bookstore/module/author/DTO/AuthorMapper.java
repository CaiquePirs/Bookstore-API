package com.bookStore.bookstore.module.author.DTO;

import com.bookStore.bookstore.module.author.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO dto);
    AuthorDTO toDTO(Author author);
}
