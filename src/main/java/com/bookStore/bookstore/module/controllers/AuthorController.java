package com.bookStore.bookstore.module.controllers;

import com.bookStore.bookstore.docs.AuthorApi;
import com.bookStore.bookstore.module.dtos.AuthorRequestDTO;
import com.bookStore.bookstore.module.dtos.AuthorResponseDTO;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.mappers.AuthorMapper;
import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.services.AuthorService;
import com.bookStore.bookstore.module.utils.UtilsMethods;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@Controller
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {

    private final AuthorService service;
    private final AuthorMapper mapper;
    private final UtilsMethods generic;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody @Valid AuthorRequestDTO dto) {
           Author author = service.create(dto);
           URI uri = generic.generateHeaderLocation(author.getId());
           return ResponseEntity.created(uri).body(mapper.toDTO(author));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable UUID id) {
        Author author = service.findById(id);
        return ResponseEntity.ok(mapper.toDTO(author));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuthorResponseDTO>> filterSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality,
            @RequestParam(value = "biography", required = false) String biography,
            @RequestParam(value = "status", required = false) StatusEntity status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "sizePage", required = false, defaultValue = "10") Integer sizePage){

        Page<Author> pageResult = service.filterSearch(name, nationality, biography, status, page, sizePage);
        Page<AuthorResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable UUID id, @RequestBody @Valid AuthorRequestDTO dto) {
            Author author = service.update(id, dto);
            URI uri = generic.generateHeaderLocation(author.getId());
            return ResponseEntity.created(uri).body(mapper.toDTO(author));
    }
}
