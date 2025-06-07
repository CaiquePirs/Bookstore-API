package com.bookStore.bookstore.module.book.mapper;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "author", ignore = true)
    Book toEntity(BookDTO dto);

    @Mapping(source = "author.id", target = "authorId")
    BookDTO toDTO(Book book);
}
