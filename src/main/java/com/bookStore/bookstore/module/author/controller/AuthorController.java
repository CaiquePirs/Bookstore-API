package com.bookStore.bookstore.module.author.controller;

import com.bookStore.bookstore.module.author.DTO.AuthorDTO;
import com.bookStore.bookstore.module.author.mapper.AuthorMapper;
import com.bookStore.bookstore.module.common.error.ErrorResponse;
import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController implements GenericController {

    private final AuthorService service;
    private final AuthorMapper mapper;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid AuthorDTO dto) {
            Author author = mapper.toEntity(dto);
            service.create(author);

            var uri = generateHeaderLocation(author.getId());
            return ResponseEntity.created(uri).body(mapper.toDTO(author));
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
            Author author = service.searchById(id)
                    .orElseThrow(() -> new AuthorNotFoundException(id));

            author.setName(dto.name());
            author.setNationality(dto.nationality());
            author.setBiography(dto.biography());
            author.setDateBirth(dto.dateBirth());

            service.update(author);
            return ResponseEntity.ok(mapper.toDTO(author));
    }
}
