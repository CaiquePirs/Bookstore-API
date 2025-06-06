package com.bookStore.bookstore.module.author.controller;

import com.bookStore.bookstore.module.author.DTO.AuthorDTO;
import com.bookStore.bookstore.module.author.DTO.AuthorMapper;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService service;

    @Autowired
    private AuthorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO dto) {
        try {
            Author author = mapper.toEntity(dto);
            service.create(author);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(author.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(mapper.toDTO(author));

        } catch (DuplicateRecordException e) {
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> searchAuthor(@PathVariable UUID id) {
        return service.searchById(id).map(author -> {
            AuthorDTO dto = mapper.toDTO(author);

            return ResponseEntity.ok(dto);
        }).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> filterSearch(@RequestParam(value = "name", required = false) String name,
                                                        @RequestParam(value = "nacionality", required = false) String nationality) {

        List<Author> result = service.filterSearch(name, nationality);

        List<AuthorDTO> authorDTOs = result.stream()
                .map(author -> mapper.toDTO(author))
                .collect(Collectors.toList());

        return ResponseEntity.ok(authorDTOs);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable UUID id) {
        var author = service.searchById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));

        service.delete(author);

        return ResponseEntity.ok("Author deleted successfully");
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> update(@PathVariable UUID id, @RequestBody @Valid AuthorDTO dto) {
        try {

            Author author = service.searchById(id)
                    .orElseThrow(() -> new AuthorNotFoundException(id));

            author.setName(dto.name());
            author.setNationality(dto.nationality());
            author.setBiography(dto.biography());
            author.setDateBirth(dto.dateBirth());

            service.update(author);

            return ResponseEntity.ok(mapper.toDTO(author));

        } catch (DuplicateRecordException e) {
            var errorDTO = ErrorResponse.conflict(e.getMessage());
            return ResponseEntity.status(errorDTO.status()).body(errorDTO);

        }

    }
}
