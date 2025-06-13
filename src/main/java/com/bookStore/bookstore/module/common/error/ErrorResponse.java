package com.bookStore.bookstore.module.common.error;

import java.util.List;

public record ErrorResponse(int status, String message, List<ErrorField> errors) {
}
