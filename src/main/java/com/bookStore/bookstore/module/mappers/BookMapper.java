package com.bookStore.bookstore.module.mappers;

import com.bookStore.bookstore.module.dtos.BookRequestDTO;
import com.bookStore.bookstore.module.dtos.BookResponseDTO;
import com.bookStore.bookstore.module.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = {AuthorMapper.class})
public abstract class BookMapper {

    @Mapping(target = "author", ignore = true)
    public abstract Book toEntity(BookRequestDTO dto);

    @Mapping(source = "author", target = "author")
    public abstract BookResponseDTO toDTO(Book book);
}
