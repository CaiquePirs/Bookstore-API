package com.bookStore.bookstore.module.services;

import com.bookStore.bookstore.module.common.exceptions.NotFoundException;
import com.bookStore.bookstore.module.dtos.AuthorRequestDTO;
import com.bookStore.bookstore.module.mappers.AuthorMapper;
import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.repositories.AuthorRepository;
import com.bookStore.bookstore.module.specifications.AuthorSpecs;
import com.bookStore.bookstore.module.validators.AuthorValidator;
import com.bookStore.bookstore.module.enums.StatusEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

   private final AuthorRepository repository;
   private final AuthorValidator validator;
   private final AuthorMapper mapper;
   private final ClientAuditService clientAuditService;

    public Author create(AuthorRequestDTO dto){
        Author author = mapper.toEntity(dto);
        validator.existsDuplicateAuthor(author);
        author.setStatus(StatusEntity.ACTIVE);
        author.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        return repository.save(author);
    }

    public Author findById(UUID id){
        return repository.findById(id)
                .filter(a -> !a.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Author ID not found"));
    }

    public Page<Author> filterSearch(String name, String nationality, String biography,
                                     StatusEntity status, Integer page, Integer sizePage){
        return repository.findAll(
                AuthorSpecs.specifications(name, nationality, biography, status),
                PageRequest.of(page, sizePage));
    }

    public void delete(UUID id){
        Author author = findById(id);
        author.setStatus(StatusEntity.DELETED);
        author.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        repository.save(author);
    }

    public Author update(UUID id, AuthorRequestDTO dto){
        Author author = findById(id);
        validator.existsDuplicateAuthor(author);
        author = validator.updateValidator(author, dto);
        author.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        return repository.save(author);
    }

}
