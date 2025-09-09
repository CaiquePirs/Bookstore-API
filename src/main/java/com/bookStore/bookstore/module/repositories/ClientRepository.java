package com.bookStore.bookstore.module.repositories;

import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByEmailAndStatus(String email, StatusEntity status);
    Optional<Client> findByUsernameAndStatus(String username, StatusEntity status);
}
