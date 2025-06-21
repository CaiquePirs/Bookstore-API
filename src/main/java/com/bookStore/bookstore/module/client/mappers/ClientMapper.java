package com.bookStore.bookstore.module.client.mappers;

import com.bookStore.bookstore.module.client.DTO.ClientRequestDTO;
import com.bookStore.bookstore.module.client.DTO.ClientResponseDTO;
import com.bookStore.bookstore.module.client.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    public abstract Client toEntity(ClientRequestDTO DTO);
    public abstract ClientResponseDTO toDTO(Client client);
}
