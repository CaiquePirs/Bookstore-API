package com.bookStore.bookstore.module.specifications;

import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.enums.StatusEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookSpecs {

    public static Specification<Book> specification(String title, String isbn, String publisher,
                                                    String authorName, StatusBook status){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isbn != null && !isbn.isBlank()) {
                predicates.add(cb.equal(root.get("isbn"), isbn));
            }
            if(title != null && !title.isBlank()) {
                predicates.add(cb.equal(root.get("title"), title));
            }
            if(publisher != null && !publisher.isBlank()){
                predicates.add(cb.equal(root.get("biography"), publisher));
            }
            if(authorName != null && !authorName.isBlank()){
                predicates.add(cb.like(cb.lower(root.join("author").get("name")),
                        "%" + authorName.toLowerCase() + "%"));
            }
            predicates.add(cb.equal(root.get("status"), Objects.requireNonNullElse(status, StatusEntity.ACTIVE)));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

