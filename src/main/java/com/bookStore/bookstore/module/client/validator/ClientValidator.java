package com.bookStore.bookstore.module.client.validator;

import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientValidator {

    private final ClientRepository repository;

    public void validateClient(Client client) {
        repository.findByEmailAndStatus(client.getEmail(), StatusClient.ACTIVE)
                .ifPresent(u -> {
                    throw new DuplicateRecordException("This email has already been registered");
                });

        repository.findByUsernameAndStatus(client.getUsername(), StatusClient.ACTIVE)
                .ifPresent(u -> {
                    throw new DuplicateRecordException("This username has already been registered");
                });
    }
}