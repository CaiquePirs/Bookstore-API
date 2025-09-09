package com.bookStore.bookstore.module.validators;

import com.bookStore.bookstore.module.common.exceptions.DuplicateRecordException;
import com.bookStore.bookstore.module.dtos.ClientUpdateDTO;
import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.repositories.ClientRepository;
import com.bookStore.bookstore.module.services.ClientAuditService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class ClientValidator {

    private final ClientRepository repository;
    private final ClientAuditService clientAuditService;
    private final PasswordEncoder encoder;

    public void validateIfExistClient(String email, String username) {
        repository.findByEmailAndStatus(email, StatusEntity.ACTIVE).ifPresent(u -> {
                    throw new DuplicateRecordException("This email has already been registered");
                });

        repository.findByUsernameAndStatus(username, StatusEntity.ACTIVE).ifPresent(u -> {
                    throw new DuplicateRecordException("This username has already been registered");
                });
    }

    public void validateIfIsTheSameClient(UUID clientId){
        var currentUserId = clientAuditService.getCurrentUserAuditId();

        boolean isSelf = clientId.equals(currentUserId);
        boolean isAdmin = clientAuditService.getCurrentUserRoles().contains("ADMIN");

        if (!isSelf && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not authorized");
        }
    }

    public Client updateValidator(Client client, ClientUpdateDTO dto) {
        if (dto.username() != null && !dto.username().equals(client.getUsername())) {
            boolean existByUsername = repository.findByUsernameAndStatus(dto.username(), StatusEntity.ACTIVE).isPresent();
            if (existByUsername) throw new DuplicateRecordException("This client already exist.");
            client.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(client.getEmail())) {
            boolean existByEmail = repository.findByUsernameAndStatus(dto.email(), StatusEntity.ACTIVE).isPresent();
            if (existByEmail) throw new DuplicateRecordException("This client already exist.");
            client.setEmail(dto.email());
        }

        if (dto.password() != null) client.setPassword(encoder.encode(dto.password()));
        if (dto.dateBirth() != null) client.setDateBirth(dto.dateBirth());
        return client;
    }

}