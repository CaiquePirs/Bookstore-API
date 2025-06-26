package com.bookStore.bookstore.docs;

import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.model.StatusOrder;
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

@Tag(name = "Orders", description = "Endpoints for managing book orders")
public interface OrderControllerDoc {

    @Operation(summary = "Create Order", description = "Register a new book lending order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "clientId", "message": "Client ID cannot be null" },
                    { "field": "bookId", "message": "Book ID is required" }
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
    ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderDTO dto);

    @Operation(summary = "Return Book Order", description = "Mark a book order as returned")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Order not found",
                  "errors": []
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
    @PostMapping("/{id}/return")
    ResponseEntity<Void> returnOrder(@PathVariable UUID id);

    @Operation(summary = "Find Order by ID", description = "Search for an order using its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Order not found",
                  "errors": []
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
    @GetMapping("{id}")
    ResponseEntity<OrderResponseDTO> searchById(@PathVariable UUID id);

    @Operation(summary = "List Orders", description = "List or filter orders by status and pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully"),
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
    @GetMapping
    ResponseEntity<Page<OrderResponseDTO>> filterSearch(
            @RequestParam(value = "status", required = false) StatusOrder status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage
    );

    @Operation(summary = "Update Order", description = "Update information of an existing order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Validation failed",
                  "errors": [
                    { "field": "status", "message": "Status must not be null" }
                  ]
                }
            """)
            )),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Order not found",
                  "errors": []
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
    @PutMapping("{id}")
    ResponseEntity<OrderResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid OrderDTO dto);

    @Operation(summary = "Delete Order", description = "Delete an order by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 404,
                  "message": "Order not found",
                  "errors": []
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
    @DeleteMapping("{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id);
}


