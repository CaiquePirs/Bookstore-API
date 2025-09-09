package com.bookStore.bookstore.module.specifications;

import com.bookStore.bookstore.module.entities.Author;
import com.bookStore.bookstore.module.enums.StatusEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorSpecs {

    public static Specification<Author> specifications(String name, String nationality, String biography, StatusEntity status){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.equal(root.get("name"), name));
            }
            if(nationality != null && !nationality.isBlank()) {
                predicates.add(cb.equal(root.get("nationality"), nationality));
            }
            if(biography != null && !biography.isBlank()){
                predicates.add(cb.equal(root.get("biography"), biography));
            }

            predicates.add(cb.equal(root.get("status"), Objects.requireNonNullElse(status, StatusEntity.ACTIVE)));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
