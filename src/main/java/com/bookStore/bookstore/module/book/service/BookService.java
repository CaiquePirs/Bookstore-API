package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.model.Author;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.AuthorRepository;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    BookRepository repository;

    @Autowired
    AuthorRepository authorRepository;

    public Book create(BookDTO dto){
        Author author = authorRepository.findById(dto.author_id())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Book book = new Book(dto.title(), dto.isbn(), dto.publisher(), dto.publicationDate(), author);

        return repository.save(book);
    }
}
