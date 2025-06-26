package com.bookStore.bookstore.module.common.error;

import com.bookStore.bookstore.docs.ErrorResponseDTODoc;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(implementation = ErrorResponseDTODoc.class)
public record ErrorResponseDTO(int status, String message, List<ErrorField> errors) {
}
