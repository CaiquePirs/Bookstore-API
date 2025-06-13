package com.bookStore.bookstore.module.user.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserDTO(@NotBlank(message = "Username is required")
                      @Size(max = 30, message = "Username must be up to 30 characters long")
                      String username,
                      @NotBlank(message = "Email is required")
                      String email,
                      @NotBlank(message = "Password os required")
                      String password,
                      @NotNull(message = "Date birth is required")
                      LocalDate dateBirth) {
}
