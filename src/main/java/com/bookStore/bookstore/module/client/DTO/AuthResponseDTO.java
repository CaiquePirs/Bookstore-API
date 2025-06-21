package com.bookStore.bookstore.module.client.DTO;

import java.util.List;

public record AuthResponseDTO(
        String token,
        String username,
        String email,
        List<String> roles) {
}
