package com.bookStore.bookstore.module.common.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "ErrorResponseDTO", description = "Standard error structure returned when an operation fails")
public record ErrorResponseDTO(

        @Schema(description = "HTTP status code of the error", example = "400")
        int status,

        @Schema(description = "Brief description of the error", example = "Validation failed")
        String message,

        @Schema(description = "List of field-specific errors (only in case of validation errors)")
        List<ErrorField> errors) {
}
