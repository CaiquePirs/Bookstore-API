package com.bookStore.bookstore.module.dtos;

import java.time.LocalDate;

public record ClientUpdateDTO(
        String username,
        String email,
        String password,
        LocalDate dateBirth) {
}
