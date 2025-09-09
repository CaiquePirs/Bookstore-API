package com.bookStore.bookstore.module.common.login;

import java.util.List;

public record LoginResponseDTO(
        String token,
        String username,
        String email,
        List<String> roles) {
}
