package com.bookStore.bookstore.module.client.controller;

import com.bookStore.bookstore.module.client.DTO.ClientRequestDTO;
import com.bookStore.bookstore.module.client.DTO.ClientResponseDTO;
import com.bookStore.bookstore.module.client.mappers.ClientMapper;
import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.service.ClientService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController implements GenericController {

    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientRequestDTO dto){
        var client = service.create(mapper.toEntity(dto));
        var uri = generateHeaderLocation(client.getId());
        return ResponseEntity.created(uri).body(mapper.toDTO(client));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> searchById(@PathVariable UUID id){
      var client = service.searchById(id);
      return ResponseEntity.ok(mapper.toDTO(client));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientResponseDTO>> listClient(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) StatusClient status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage) {

        Page<Client> pageResult = service.searchClientByQuery(username, status, page, sizePage);
        Page<ClientResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
       service.softDelete(id);
       return ResponseEntity.noContent().build();
    }


    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ClientResponseDTO> update(@PathVariable UUID id, @Valid @RequestBody ClientRequestDTO dto){
       var client = service.update(id, dto);
       var uri = generateHeaderLocation(id);
       return ResponseEntity.created(uri).body(mapper.toDTO(client));
    }
}
