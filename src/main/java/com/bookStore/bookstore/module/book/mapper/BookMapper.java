package com.bookStore.bookstore.module.book.mapper;

import com.bookStore.bookstore.module.author.mapper.AuthorMapper;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.DTO.ResponseBookDTO;
import com.bookStore.bookstore.module.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",  uses = {AuthorMapper.class})
public abstract class BookMapper {

    @Mapping(target = "author", ignore = true)
    public abstract Book toEntity(BookDTO dto);

    @Mapping(source = "author", target = "author")
    public abstract ResponseBookDTO toDTO(Book book);
}
