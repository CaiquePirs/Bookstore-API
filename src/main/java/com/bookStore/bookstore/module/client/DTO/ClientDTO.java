package com.bookStore.bookstore.module.client.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClientDTO(@NotBlank(message = "Username is required")
                        @Size(max = 30, message = "Username must be up to 30 characters long")
                        String username,
                        @NotBlank(message = "Email is required")
                        @Email(message = "Email must be valid")
                        String email,
                        @NotBlank(message = "Password os required")
                        String password,
                        @NotNull(message = "Date birth is required")
                        LocalDate dateBirth) {
}
