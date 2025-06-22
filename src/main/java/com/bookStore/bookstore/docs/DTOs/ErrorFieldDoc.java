package com.bookStore.bookstore.docs.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a validation error on a specific field")
public interface ErrorFieldDoc{
        @Schema(description = "The name of the field", example = "name")
        String field();

        @Schema(description = "The validation error message", example = "must not be blank")
        String messag();
}
