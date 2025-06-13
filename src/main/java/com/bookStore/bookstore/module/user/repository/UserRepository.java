package com.bookStore.bookstore.module.user.repository;

import com.bookStore.bookstore.module.user.model.StatusUser;
import com.bookStore.bookstore.module.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmailAndStatus(String email, StatusUser status);
    Optional<User> findByUsernameAndStatus(String username, StatusUser status);
}
