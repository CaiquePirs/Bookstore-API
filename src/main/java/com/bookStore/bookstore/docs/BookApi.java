package com.bookStore.bookstore.docs;

import com.bookStore.bookstore.module.dtos.BookRequestDTO;
import com.bookStore.bookstore.module.dtos.BookResponseDTO;
import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import com.bookStore.bookstore.module.dtos.BookUpdateDTO;
import com.bookStore.bookstore.module.enums.StatusBook;
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

@Tag(name = "Books", description = "Endpoints for managing books in the system")
public interface BookApi {

    @Operation(summary = "Create Book", description = "Register a new book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "title", "message": "Title is required" },
                    { "field": "isbn", "message": "ISBN must be valid" }
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
    ResponseEntity<BookResponseDTO> createBook(@RequestBody @Valid BookRequestDTO bookRequestDTO);

    @Operation(summary = "Get Book by ID", description = "Retrieve a book using its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Book not found",
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
    ResponseEntity<BookResponseDTO> getBookById(@PathVariable UUID id);

    @Operation(summary = "Search Books", description = "Search for books by title, ISBN, publisher, author, and status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
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
    ResponseEntity<Page<BookResponseDTO>> searchBookByQuery(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "publisher", required = false) String publisher,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "status", required = false) StatusBook status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage
    );

    @Operation(summary = "Delete Book", description = "Delete a book by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Book not found",
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
    ResponseEntity<Void> deleteById(@PathVariable UUID id);

    @Operation(summary = "Update Book", description = "Update an existing book")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "publisher", "message": "Publisher is required" }
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
    ResponseEntity<BookResponseDTO> update(@PathVariable UUID id, @RequestBody BookUpdateDTO dto);
}


