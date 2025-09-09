package com.bookStore.bookstore.docs;

import com.bookStore.bookstore.module.dtos.AuthorRequestDTO;
import com.bookStore.bookstore.module.dtos.AuthorResponseDTO;
import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import com.bookStore.bookstore.module.enums.StatusEntity;
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

@Tag(name = "Authors", description = "Endpoints for managing authors")
public interface AuthorApi {

    @Operation(summary = "Create Author", description = "Register a new author")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "name", "message": "Name is required" },
                    { "field": "biography", "message": "The biography must be up to 100 characters" }
                  ]
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
    @PostMapping
    ResponseEntity<AuthorResponseDTO> createAuthor(@RequestBody @Valid AuthorRequestDTO dto);

    @Operation(summary = "Get Author by ID", description = "Retrieve an author by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Author not found",
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
    ResponseEntity<AuthorResponseDTO> getAuthor(@PathVariable UUID id);

    @Operation(summary = "Search Authors", description = "Search authors by name, nationality, biography, and status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors retrieved successfully"),
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
    ResponseEntity<Page<AuthorResponseDTO>> filterSearch(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "nationality", required = false) String nationality,
            @RequestParam(value = "biography", required = false) String biography,
            @RequestParam(value = "status", required = false) StatusEntity status,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "sizePage", required = false, defaultValue = "10") Integer sizePage
    );

    @Operation(summary = "Delete Author", description = "Delete an author by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Author not found",
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
    ResponseEntity<Void> deleteAuthor(@PathVariable UUID id);

    @Operation(summary = "Update Author", description = "Update an existing author's data")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "name", "message": "Name is required" },
                    { "field": "dateBirth", "message": "Date of birth is required" }
                  ]
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
    ResponseEntity<AuthorResponseDTO> updateAuthor(@PathVariable UUID id, @RequestBody @Valid AuthorRequestDTO dto);
}

