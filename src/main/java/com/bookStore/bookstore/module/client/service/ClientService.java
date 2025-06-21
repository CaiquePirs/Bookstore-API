package com.bookStore.bookstore.module.client.service;

import com.bookStore.bookstore.module.client.DTO.ClientRequestDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientValidator validator;
    private final PasswordEncoder encoder;
    private final ClientAuditService clientAuditService;

    public Client create(Client client){
        validator.validateClient(client);

        try {
            client.setUserAuditId(clientAuditService.getCurrentUserAuditId());
            client.setPassword(encoder.encode(client.getPassword()));
            client.setStatus(StatusClient.ACTIVE);
            client.setRoles(List.of("USER"));
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

        var currentUserId = clientAuditService.getCurrentUserAuditId();

        boolean isSelf = client.getId().equals(currentUserId);
        boolean isAdmin = clientAuditService.getCurrentUserRoles().contains("ADMIN");

        if (!isSelf && !isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own account.");
        }

        client.setUserAuditId(clientAuditService.getCurrentUserAuditId());
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

    public Client update(UUID id, ClientRequestDTO dto) {
        var client = searchById(id);

        if (client.getStatus() == StatusClient.DELETED_AT) {
            throw new ClientDeletedException("This user is already deleted");
        }

        if (dto.username() != null && !dto.username().equals(client.getUsername())) {
            client.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(client.getEmail())) {
            client.setEmail(dto.email());
        }

        if (dto.password() != null) {
            client.setPassword(encoder.encode(dto.password()));
        }

        if (dto.dateBirth() != null) {
            client.setDateBirth(dto.dateBirth());
        }

        validator.validateClient(client);

        try {
            var currentUserId = clientAuditService.getCurrentUserAuditId();

            boolean isSelf = client.getId().equals(currentUserId);
            boolean isAdmin = clientAuditService.getCurrentUserRoles().contains("ADMIN");

            if (!isSelf && !isAdmin) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own account.");
            }

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
