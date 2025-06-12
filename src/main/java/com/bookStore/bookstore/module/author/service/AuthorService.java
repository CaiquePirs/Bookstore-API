package com.bookStore.bookstore.module.author.service;

import com.bookStore.bookstore.module.author.model.Author;
import com.bookStore.bookstore.module.author.repository.AuthorRepository;
import com.bookStore.bookstore.module.author.validator.AuthorValidator;
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
   private final AuthorValidator validator;

    public Author create(Author author){
        validator.validate(author);
        return repository.save(author);
    }

    public Author searchById(UUID id){
        return repository.findById(id).map(author -> {
            if(author.getStatus().equals(StatusAuthor.DELETED_AT)){
                throw new AuthorDeletedException("This author has been deleted from the system");
            }
            return author;
        }).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    public Page<Author> filterSearch(String name,
                                     String nationality,
                                     String biography,
                                     StatusAuthor status,
                                     Integer page,
                                     Integer sizePage){

        Specification<Author> specs = (root, query, cb) -> cb.conjunction();

        if (name != null && !name.isBlank()) {
            specs = specs.and(AuthorSpecs.nameEqual(name));
        }

        if(nationality != null && !nationality.isBlank()){
            specs = specs.and(AuthorSpecs.nationalityEqual(nationality));
        }

        if(biography != null && !biography.isBlank()){
            specs = specs.and(AuthorSpecs.biographyEqual(biography));
        }

        if (status != null) {
            specs = specs.and((root, query, cb) -> cb.equal(root.get("status"), status));
        } else {
            specs = specs.and((root, query, cb) -> cb.notEqual(root.get("status"), StatusBook.DELETED_AT));
        }

        Pageable pageRequest = PageRequest.of(page, sizePage);
        return repository.findAll(specs, pageRequest);
    }

    public Author update(UUID id, AuthorDTO dto){
        var author = searchById(id);

        if (dto.name() != null) {
            author.setName(dto.name());
        }
        if (dto.nationality() != null) {
            author.setNationality(dto.nationality());
        }
        if (dto.biography() != null) {
            author.setBiography(dto.biography());
        }
        if (dto.dateBirth() != null) {
            author.setDateBirth(dto.dateBirth());
        }
        validator.validate(author);
        return repository.save(author);
    }

}
