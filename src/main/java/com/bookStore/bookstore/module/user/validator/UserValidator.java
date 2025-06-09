package com.bookStore.bookstore.module.user.validator;

import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.user.model.User;
import com.bookStore.bookstore.module.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validateUser(User user) {

        Optional<User> userFound = repository.findByEmailOrUsername(user.getEmail(), user.getUsername());

        if(userFound.isPresent()){
            var u = userFound.get();
            if(u.getEmail().equals(user.getEmail())){
                throw new DuplicateRecordException("This email has already been registered");

            } else if(u.getUsername().equals(user.getUsername())){
                throw new DuplicateRecordException("This username has already been registered");
            }
        }
        }

    }