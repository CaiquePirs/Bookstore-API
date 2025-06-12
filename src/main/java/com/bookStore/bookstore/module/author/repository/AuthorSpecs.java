package com.bookStore.bookstore.module.author.repository;

import com.bookStore.bookstore.module.author.model.Author;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecs {
    public static Specification<Author> nameEqual(String name) {
        return (root, query, cb) -> cb.equal(root.get("name"), name);
    }

    public static Specification<Author> nationalityEqual(String nationality) {
        return (root, query, cb) -> cb.equal(root.get("nationality"), nationality);
    }

    public static Specification<Author> biographyEqual(String biography) {
        return (root, query, cb) -> cb.equal(root.get("biography"), biography);
    }
}
