package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.book.model.Author;
import com.bookStore.bookstore.module.book.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository repository;

    public Author create(Author author){
        return repository.save(author);
    }

    public Optional<Author> search(UUID id){
        return repository.findById(id);
    }

    public List<Author> filterSearch(String name, String nationality){

        if(name != null && nationality != null) {
            return repository.findByNameAndNationality(name, nationality);

        } else if(name != null){
            return repository.findByName(name);

        } else if(nationality != null){
            return repository.findByNationality(nationality);
        }

        return repository.findAll();
    }


    public void delete(Author author){
        repository.delete(author);
    }

    public void update(Author author){
        repository.save(author);
    }



}
