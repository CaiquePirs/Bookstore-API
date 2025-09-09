package com.bookStore.bookstore.module.common.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        String username,

        @NotBlank
        String password) {
}
