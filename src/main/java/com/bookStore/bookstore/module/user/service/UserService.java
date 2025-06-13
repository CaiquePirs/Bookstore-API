package com.bookStore.bookstore.module.user.service;

import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.user.DTO.UserDTO;
import com.bookStore.bookstore.module.user.exception.UserDeletedException;
import com.bookStore.bookstore.module.user.exception.UserNotFoundException;
import com.bookStore.bookstore.module.user.mappers.UserMapper;
import com.bookStore.bookstore.module.user.model.StatusUser;
import com.bookStore.bookstore.module.user.model.User;
import com.bookStore.bookstore.module.user.repository.UserRepository;
import com.bookStore.bookstore.module.user.repository.UserSpecs;
import com.bookStore.bookstore.module.user.validator.UserValidator;
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
public class UserService {

    private final UserRepository repository;
    private final UserValidator validator;
    private final UserMapper mapper;

    public User create(User user){
        validator.validateUser(user);

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

    public User searchById(UUID id){
        return repository.findById(id).map(user -> {
            if(user.getStatus().equals(StatusUser.DELETED_AT)){
                throw new UserDeletedException("This user is already deleted");
            }
            return user;
        }).orElseThrow(() -> new UserNotFoundException("User ID not found"));
    }

    public void softDelete(UUID id){
        var user = searchById(id);
        user.setStatus(StatusUser.DELETED_AT);
        repository.save(user);
    }

    public Page<User> searchUserByQuery(String username, StatusUser status, Integer page, Integer sizePage){
        Specification<User> specs = (root, query, cb) -> cb.conjunction();

        if (username != null && !username.isBlank()) {
            specs = specs.and(UserSpecs.usernameEqual(username));
        }

        if (status != null) {
            specs = specs.and((root, query, cb) -> cb.equal(root.get("status"), status));
        } else {
            specs = specs.and((root, query, cb) -> cb.notEqual(root.get("status"), StatusUser.DELETED_AT));
        }

        Pageable pageRequest = PageRequest.of(page, sizePage);
        return repository.findAll(specs, pageRequest);
    }

    public User update(UUID id, UserDTO dto) {
        var user = searchById(id);

        if (user.getStatus() == StatusUser.DELETED_AT) {
            throw new UserDeletedException("This user is already deleted");
        }

        if (dto.username() != null && !dto.username().equals(user.getUsername())) {
            user.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            user.setEmail(dto.email());
        }

        if (dto.password() != null) {
            user.setPassword(dto.password());
        }

        if (dto.dateBirth() != null) {
            user.setDateBirth(dto.dateBirth());
        }

        validator.validateUser(user);

        try {
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

}
