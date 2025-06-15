package com.bookStore.bookstore.module.client.service;

import com.bookStore.bookstore.module.client.DTO.ClientDTO;
import com.bookStore.bookstore.module.client.exception.ClientDeletedException;
import com.bookStore.bookstore.module.client.exception.ClientNotFoundException;
import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.repository.ClientRepository;
import com.bookStore.bookstore.module.client.repository.ClientSpecs;
import com.bookStore.bookstore.module.client.validator.ClientValidator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    private final UserRepository repository;
    private final UserValidator validator;
    private final UserMapper mapper;
    private final ClientRepository repository;
    private final ClientValidator validator;

    public User create(User user){
        validator.validateUser(user);
    public Client getClientByUsername(String username){
        return repository.findByEmailAndStatus(username, StatusClient.ACTIVE)
                .orElseThrow(() -> new ClientNotFoundException("User not found"));
    }


        try {
            user.setStatus(StatusUser.ACTIVE);
            return repository.save(user);

        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();

            if (message != null && message.contains("email")) {
                throw new DuplicateRecordException("This email has already been registered");
            }
            if (message != null && message.contains("username")) {
                throw new DuplicateRecordException("This username has already been registered");
            }
            throw e;
        }
    }

    public Client searchById(UUID id){
        return repository.findById(id).map(client -> {
            if(client.getStatus().equals(StatusClient.DELETED_AT)){
                throw new ClientDeletedException("This user is already deleted");
            }
            return client;
        }).orElseThrow(() -> new ClientNotFoundException("User ID not found"));
    }

    public void softDelete(UUID id){
        var client = searchById(id);
        client.setStatus(StatusClient.DELETED_AT);
        repository.save(client);
    }

    public Page<Client> searchClientByQuery(String username, StatusClient status, Integer page, Integer sizePage){
        Specification<Client> specs = (root, query, cb) -> cb.conjunction();

        if (username != null && !username.isBlank()) {
            specs = specs.and(ClientSpecs.usernameEqual(username));
        }

        if (status != null) {
            specs = specs.and((root, query, cb) -> cb.equal(root.get("status"), status));
        } else {
            specs = specs.and((root, query, cb) -> cb.notEqual(root.get("status"), StatusClient.DELETED_AT));
        }

        Pageable pageRequest = PageRequest.of(page, sizePage);
        return repository.findAll(specs, pageRequest);
    }

    public Client update(UUID id, ClientDTO dto) {
        var client = searchById(id);

        if (client.getStatus() == StatusClient.DELETED_AT) {
            throw new ClientDeletedException("This user is already deleted");
        }

        if (dto.username() != null && !dto.username().equals(user.getUsername())) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(client.getEmail())) {
            client.setEmail(dto.email());
        }

        if (dto.password() != null) {
            client.setPassword(dto.password());
        }

        if (dto.dateBirth() != null) {
            client.setDateBirth(dto.dateBirth());
        }

        validator.validateClient(client);

        try {
            return repository.save(client);

        } catch (DataIntegrityViolationException e) {
            String message = e.getMostSpecificCause().getMessage();

            if (message != null && message.contains("email")) {
                throw new DuplicateRecordException("This email has already been registered");
            }
            if (message != null && message.contains("username")) {
                throw new DuplicateRecordException("This username has already been registered");
            }
            throw e;
        }
    }

}
