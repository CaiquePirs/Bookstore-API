package com.bookStore.bookstore.module.controllers;

import com.bookStore.bookstore.docs.ClientApi;
import com.bookStore.bookstore.module.dtos.ClientRequestDTO;
import com.bookStore.bookstore.module.dtos.ClientResponseDTO;
import com.bookStore.bookstore.module.dtos.ClientUpdateDTO;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.mappers.ClientMapper;
import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.services.ClientService;
import com.bookStore.bookstore.module.utils.UtilsMethods;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController implements ClientApi {

    private final ClientService service;
    private final ClientMapper mapper;
    private final UtilsMethods generic;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@RequestBody @Valid ClientRequestDTO dto){
        Client client = service.create(dto);
        URI uri = generic.generateHeaderLocation(client.getId());
        return ResponseEntity.created(uri).body(mapper.toDTO(client));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDTO> findClientById(@PathVariable UUID id){
      Client client = service.findById(id);
      return ResponseEntity.ok(mapper.toDTO(client));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ClientResponseDTO>> listClientsByFilter(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) StatusEntity status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage) {

        Page<Client> pageResult = service.searchClientByQuery(username, status, page, sizePage);
        Page<ClientResponseDTO> result = pageResult.map(mapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id){
       service.softDelete(id);
       return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientUpdateDTO dto){
       Client client = service.update(id, dto);
       URI uri = generic.generateHeaderLocation(id);
       return ResponseEntity.created(uri).body(mapper.toDTO(client));
    }
}
