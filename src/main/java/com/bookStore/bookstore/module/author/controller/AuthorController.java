package com.bookStore.bookstore.module.author.controller;

import com.bookStore.bookstore.module.author.DTO.AuthorDTO;
import com.bookStore.bookstore.module.author.DTO.ErrorResponse;
import com.bookStore.bookstore.module.author.exceptions.AuthorNotFoundException;
import com.bookStore.bookstore.module.author.exceptions.DuplicateRecordException;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.service.AuthorService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO authorDTO) {
        try {
            var author = service.create(authorDTO.author());

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(author.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(author);

        } catch (DuplicateRecordException e) {
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> searchAuthor(@PathVariable UUID id) {

        Author author = service.searchById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        AuthorDTO authorDTO = new AuthorDTO(author.getId(),
                author.getName(),
                author.getNationality(),
                author.getBiography(),
                author.getDateBirth());

        return ResponseEntity.ok(authorDTO);
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> filterSearch(@RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "nacionality", required = false) String nationality) {

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
    public ResponseEntity<String> deleteAuthor(@PathVariable UUID id) {
        var authorId = service.searchById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        service.delete(authorId);

        return ResponseEntity.ok("Author deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody AuthorDTO dto) {
        try {
            Author authorSearch = service.searchById(id)
                    .orElseThrow(() -> new AuthorNotFoundException(id));

            var author = authorSearch;
            author.setName(dto.name());
            author.setNationality(dto.nationality());
            author.setBiography(dto.biography());
            author.setDateBirth(dto.dateBirth());

            service.update(author);

            return ResponseEntity.ok("Author updated successfully");

        } catch (DuplicateRecordException e) {
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);

        }

    }
}
