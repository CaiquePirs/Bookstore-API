package com.bookStore.bookstore.module.author.controller;

import com.bookStore.bookstore.module.author.DTO.AuthorDTO;
import com.bookStore.bookstore.module.author.DTO.AuthorResponseDTO;
import com.bookStore.bookstore.module.author.mapper.AuthorMapper;
import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.model.StatusAuthor;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<AuthorResponseDTO> searchAuthor(@PathVariable UUID id) {
        var author = service.searchById(id);
        return ResponseEntity.ok(mapper.toDTO(author));
    }

    @GetMapping
    public ResponseEntity<Page<AuthorResponseDTO>> filterSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality,
            @RequestParam(value = "biography", required = false) String biography,
            @RequestParam(value = "status", required = false) StatusAuthor status,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sizePage", required = false) Integer sizePage){

        Page<Author> pageResult = service.filterSearch(name, nationality, biography, status, page, sizePage);
        Page<AuthorResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid AuthorDTO dto) {
            var author = service.update(id, dto);
            var uri = generateHeaderLocation(id);
            return ResponseEntity.created(uri).body(mapper.toDTO(author));
    }
}
