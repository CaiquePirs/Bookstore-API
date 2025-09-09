package com.bookStore.bookstore.module.mappers;

import com.bookStore.bookstore.module.dtos.AuthorRequestDTO;
import com.bookStore.bookstore.module.dtos.AuthorResponseDTO;
import com.bookStore.bookstore.module.entities.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorRequestDTO dto);
    AuthorResponseDTO toDTO(Author author);
}
