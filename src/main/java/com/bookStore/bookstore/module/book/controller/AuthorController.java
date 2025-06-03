package com.bookStore.bookstore.module.book.controller;

import com.bookStore.bookstore.module.book.DTO.AuthorDTO;
import com.bookStore.bookstore.module.book.model.Author;
import com.bookStore.bookstore.module.book.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService service;

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody AuthorDTO authorDTO){
        var author = service.create(authorDTO.author());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(author.getId())
                .toUri();
        return ResponseEntity.created(uri).body(author);
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> searchAuthor(@PathVariable String id){
        var author_id = UUID.fromString(id);

       Optional<Author> authorOptional = service.search(author_id);

       if(authorOptional.isPresent()){
           Author author = authorOptional.get();

           AuthorDTO dto = new AuthorDTO(
                   author_id,
                   author.getName(),
                   author.getNacionality(),
                   author.getBiography(),
                   author.getDateBirth());
           return ResponseEntity.ok(dto);
       }
       return ResponseEntity.notFound().build();

    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> filterSearch(@RequestParam(value = "name", required = false ) String name,
                                                         @RequestParam(value = "nacionality", required = false ) String nationality) {

        List<Author> result = service.filterSearch(name, nationality);
        List<AuthorDTO> authorDTOS = result.stream()
                .map(author -> new AuthorDTO(
                        author.getId(),
                        author.getName(),
                        author.getNationality(),
                        author.getBiography(),
                        author.getDateBirth())).collect(Collectors.toList());

        return ResponseEntity.ok(authorDTOS);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable String id){
        var author_id = UUID.fromString(id);

        Optional<Author> authorOptional = service.search(author_id);

        if(authorOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        service.delete(authorOptional.get());

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody AuthorDTO dto){
        var authorId = UUID.fromString(id);
        Optional<Author> authorOptional = service.search(authorId);

        if(authorOptional.isEmpty()){
           return ResponseEntity.noContent().build();
        }

        var author = authorOptional.get();
        author.setName(dto.name());
        author.setNationality(dto.nationality());
        author.setBiography(dto.biography());
        author.setDateBirth(dto.dateBirth());

        service.update(author);

        return ResponseEntity.noContent().build();
    }



}
