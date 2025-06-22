package com.bookStore.bookstore.docs.controllers;

import com.bookStore.bookstore.module.client.DTO.ClientRequestDTO;
import com.bookStore.bookstore.module.client.DTO.ClientResponseDTO;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Clients", description = "Endpoints for managing system clients")
public interface ClientControllerDoc {

    @Operation(summary = "Create Client", description = "Register a new client")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "email", "message": "Email is invalid" },
                    { "field": "password", "message": "Password must contain at least 8 characters" }
                  ]
                }
            """)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 401,
                  "message": "Unauthorized access",
                  "errors": []
                }
            """)
            ))
    })
    @PostMapping
    ResponseEntity<ClientResponseDTO> create(@RequestBody @Valid ClientRequestDTO dto);

    @Operation(summary = "Find Client by ID", description = "Retrieve a client by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Client not found",
                  "errors": []
                }
            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "message": "Access denied",
                  "errors": []
                }
            """)
            ))
    })
    @GetMapping("{id}")
    ResponseEntity<ClientResponseDTO> searchById(@PathVariable UUID id);

    @Operation(summary = "List Clients", description = "Search or list all clients by username, status and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "message": "Access denied",
                  "errors": []
                }
            """)
            ))
    })
    @GetMapping
    ResponseEntity<Page<ClientResponseDTO>> listClient(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "status", required = false) StatusClient status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage
    );

    @Operation(summary = "Delete Client", description = "Soft delete a client by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Client not found",
                  "errors": []
                }
            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "message": "Access denied",
                  "errors": []
                }
            """)
            ))
    })
    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);

    @Operation(summary = "Update Client", description = "Update an existing client's information")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "username", "message": "Username is too short" }
                  ]
                }
            """)
            )),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Client not found",
                  "errors": []
                }
            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "message": "Access denied",
                  "errors": []
                }
            """)
            ))
    })
    @PutMapping("{id}")
    ResponseEntity<ClientResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid ClientRequestDTO dto);
}


