package com.bookStore.bookstore.module.client.service;

import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.repository.ClientRepository;
import com.bookStore.bookstore.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientAuditService {

    private final SecurityService securityService;
    private final ClientRepository repository;

    public UUID getCurrentUserAuditId() {
        var username = securityService.getLoggedUsername();
        var client = repository.findByUsernameAndStatus(username, StatusClient.ACTIVE)
                .orElse(null);
        return client != null ? client.getId() : null;
    }

    public List<String> getCurrentUserRoles(){
        var username = securityService.getLoggedUsername();
        var clientRole = repository.findByUsernameAndStatus(username, StatusClient.ACTIVE)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authenticated client not found"));
        return clientRole.getRoles();
    }
}
