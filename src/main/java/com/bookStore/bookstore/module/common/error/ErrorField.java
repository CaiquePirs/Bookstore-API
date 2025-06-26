package com.bookStore.bookstore.module.common.error;


import com.bookStore.bookstore.docs.ErrorFieldDoc;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = ErrorFieldDoc.class)
public record ErrorField(String field, String error) {
}
