package com.bookStore.bookstore.module.mappers;

import com.bookStore.bookstore.module.dtos.ClientRequestDTO;
import com.bookStore.bookstore.module.dtos.ClientResponseDTO;
import com.bookStore.bookstore.module.entities.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    public abstract Client toEntity(ClientRequestDTO DTO);
    public abstract ClientResponseDTO toDTO(Client client);
}
