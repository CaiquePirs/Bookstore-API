package com.bookStore.bookstore.module.user.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              @NotBlank(message = "Username is required")
                              String username,
                              @NotBlank(message = "Email is required")
                              String email,
                              @NotBlank(message = "Password os required")
                              String password,
                              @NotNull(message = "Date birth is required")
                              LocalDate dateBirth)  {
}
