package com.bookStore.bookstore.module.client.repository;

import com.bookStore.bookstore.module.client.model.Client;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecs {

    public static Specification<Client> usernameEqual(String username) {
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }
}
