package com.bookStore.bookstore.module.user.controller;

import com.bookStore.bookstore.module.user.DTO.UserDTO;
import com.bookStore.bookstore.module.user.DTO.UserResponseDTO;
import com.bookStore.bookstore.module.user.mappers.UserMapper;
import com.bookStore.bookstore.module.user.model.StatusUser;
import com.bookStore.bookstore.module.user.model.User;
import com.bookStore.bookstore.module.user.service.UserService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController implements GenericController {

    private final UserService service;
    private final UserMapper mapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid UserDTO dto){
        var user = service.create(mapper.toEntity(dto));
        var uri = generateHeaderLocation(user.getId());
        return ResponseEntity.created(uri).body(mapper.toDTO(user));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> searchById(@PathVariable UUID id){
      var user = service.searchById(id);
      return ResponseEntity.ok(mapper.toDTO(user));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> listUser(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) StatusUser status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage) {

        Page<User> pageResult = service.searchUserByQuery(username, status, page, sizePage);
        Page<UserResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
       service.softDelete(id);
       return ResponseEntity.noContent().build();
    }


    @PutMapping("{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody UserDTO dto){
       var user = service.update(id, dto);
       var uri = generateHeaderLocation(id);
       return ResponseEntity.created(uri).body(mapper.toDTO(user));
    }

}
