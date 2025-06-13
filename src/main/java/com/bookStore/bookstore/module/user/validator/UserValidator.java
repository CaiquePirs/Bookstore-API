package com.bookStore.bookstore.module.user.validator;

import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.user.model.StatusUser;
import com.bookStore.bookstore.module.user.model.User;
import com.bookStore.bookstore.module.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validateUser(User user) {
        repository.findByEmailAndStatus(user.getEmail(), StatusUser.ACTIVE)
                .ifPresent(u -> {
                    throw new DuplicateRecordException("This email has already been registered");
                });

        repository.findByUsernameAndStatus(user.getUsername(), StatusUser.ACTIVE)
                .ifPresent(u -> {
                    throw new DuplicateRecordException("This username has already been registered");
                });
    }
}