package com.bookStore.bookstore.module.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorRequestDTO(
                        @NotBlank(message = "Name is required")
                        String name,

                        @NotBlank(message = "Nationality is required")
                        String nationality,

                        @NotBlank(message = "Biography is required")
                        @Size(max = 100, message = "the biography must be up to 100 characters")
                        String biography,

                        @NotNull(message = "Date birth is required")
                        LocalDate dateBirth) {
}
