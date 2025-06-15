package com.bookStore.bookstore.module.client.repository;

import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.client.model.StatusClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByEmailAndStatus(String email, StatusClient status);
    Optional<Client> findByUsernameAndStatus(String username, StatusClient status);
}
