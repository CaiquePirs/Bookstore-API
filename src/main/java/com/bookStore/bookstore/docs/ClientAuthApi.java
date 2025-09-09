package com.bookStore.bookstore.docs;

import com.bookStore.bookstore.module.common.login.LoginResponseDTO;
import com.bookStore.bookstore.module.common.login.LoginRequestDTO;
import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoint for client authentication and login")
public interface ClientAuthApi {

    @Operation(summary = "Login", description = "Authenticate client and generate JWT token")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 400,
                  "message": "Invalid username or password",
                  "errors": []
                }
            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden - inactive client or access denied", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponseDTO.class),
                    examples = @ExampleObject(value = """
                {
                  "status": 403,
                  "message": "Access denied: client is inactive",
                  "errors": []
                }
            """)
            ))
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO);
}


