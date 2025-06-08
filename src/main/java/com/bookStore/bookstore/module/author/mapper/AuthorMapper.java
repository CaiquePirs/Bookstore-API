package com.bookStore.bookstore.module.author.mapper;

import com.bookStore.bookstore.module.author.DTO.AuthorDTO;
import com.bookStore.bookstore.module.author.DTO.AuthorResponseDTO;
import com.bookStore.bookstore.module.author.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toEntity(AuthorDTO dto);
    AuthorResponseDTO toDTO(Author author);
}
