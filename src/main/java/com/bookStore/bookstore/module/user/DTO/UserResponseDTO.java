package com.bookStore.bookstore.module.user.DTO;


import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(UUID id,
                              String username,
                              String email,
                              String password,
                              LocalDate dateBirth)  {
}
