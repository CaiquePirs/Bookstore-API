package com.bookStore.bookstore.module.user.repository;

import com.bookStore.bookstore.module.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecs  {

    public static Specification<User> usernameEqual(String username) {
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }
}
