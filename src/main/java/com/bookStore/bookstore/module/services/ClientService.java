package com.bookStore.bookstore.module.services;

import com.bookStore.bookstore.module.common.exceptions.NotFoundException;
import com.bookStore.bookstore.module.dtos.ClientRequestDTO;
import com.bookStore.bookstore.module.dtos.ClientUpdateDTO;
import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.mappers.ClientMapper;
import com.bookStore.bookstore.module.repositories.ClientRepository;
import com.bookStore.bookstore.module.specifications.ClientSpecs;
import com.bookStore.bookstore.module.validators.ClientValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientValidator validator;
    private final PasswordEncoder encoder;
    private final ClientAuditService clientAuditService;
    private final ClientMapper mapper;

    public Client create(ClientRequestDTO dto) {
        validator.validateIfExistClient(dto.email(), dto.username());
        Client client = mapper.toEntity(dto);

        client.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        client.setPassword(encoder.encode(client.getPassword()));
        client.setStatus(StatusEntity.ACTIVE);
        client.setRoles(List.of("USER"));
        return repository.save(client);

    }

    public Client findById(UUID id){
        return repository.findById(id)
                .filter(c -> !c.getStatus().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Client ID not found"));
    }

    public void softDelete(UUID id){
        Client client = findById(id);

        validator.validateIfIsTheSameClient(client.getId());

        client.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        client.setStatus(StatusEntity.DELETED);
        repository.save(client);
    }

    public Page<Client> searchClientByQuery(String username, StatusEntity status, Integer page, Integer sizePage){
        return repository.findAll(ClientSpecs.specifications(username, status), PageRequest.of(page, sizePage));
    }

    public Client update(UUID id, ClientUpdateDTO dto) {
        Client client = findById(id);

        validator.validateIfIsTheSameClient(client.getId());
        client = validator.updateValidator(client, dto);

        return repository.save(client);
    }

}
