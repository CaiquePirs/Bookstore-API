package com.bookStore.bookstore.module.author.service;

import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.repository.AuthorRepository;
import com.bookStore.bookstore.module.author.validator.ValidatorAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

   private final AuthorRepository repository;
   private final ValidatorAuthor validator;

    public Author create(Author author){
        validator.validate(author);
        return repository.save(author);
    }

    public Optional<Author> searchById(UUID id){
        return repository.findById(id);
    }

    public List<Author> filterSearch(String name, String nationality){
        var author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnorePaths("id", "biography", "dateBirth")
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Author> authorExample = Example.of(author, matcher);
        return repository.findAll(authorExample);
    }


    public void delete(Author author){
        repository.delete(author);
    }

    public void update(Author author){
        validator.validate(author);
        repository.save(author);
    }

}
