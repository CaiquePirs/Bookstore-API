package com.bookStore.bookstore.module.author.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorDTO(UUID id,
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
