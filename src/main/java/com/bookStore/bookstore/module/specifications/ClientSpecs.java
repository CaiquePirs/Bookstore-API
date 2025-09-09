package com.bookStore.bookstore.module.specifications;

import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientSpecs {

    public static Specification<Client> specifications(String username, StatusEntity status){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.isBlank()) {
                predicates.add(cb.equal(root.get("username"), username));
            }
            predicates.add(cb.equal(root.get("status"), Objects.requireNonNullElse(status, StatusEntity.ACTIVE)));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Client> usernameEqual(String username) {
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }
}
