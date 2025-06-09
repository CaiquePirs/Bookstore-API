package com.bookStore.bookstore.module.user.mappers;

import com.bookStore.bookstore.module.user.DTO.UserDTO;
import com.bookStore.bookstore.module.user.DTO.UserResponseDTO;
import com.bookStore.bookstore.module.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User toEntity(UserDTO DTO);
    public abstract UserResponseDTO toDTO(User user);
}
