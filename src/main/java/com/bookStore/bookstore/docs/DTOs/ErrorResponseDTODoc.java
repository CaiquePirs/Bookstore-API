package com.bookStore.bookstore.docs.DTOs;

import com.bookStore.bookstore.module.common.error.ErrorField;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ErrorResponseDTO", description = "Standard error structure returned when an operation fails")
public interface ErrorResponseDTODoc {

    @Schema(description = "HTTP status code of the error", example = "400")
    int getStatus();

    @Schema(description = "Brief description of the error", example = "Validation failed")
    String getMessage();

    @Schema(description = "List of field-specific errors (only in case of validation errors)")
    List<ErrorField> getErrors();
}

