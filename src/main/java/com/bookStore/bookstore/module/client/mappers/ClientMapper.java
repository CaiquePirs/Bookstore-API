package com.bookStore.bookstore.module.client.mappers;

import com.bookStore.bookstore.module.client.DTO.ClientDTO;
import com.bookStore.bookstore.module.client.DTO.ClientResponseDTO;
import com.bookStore.bookstore.module.client.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ClientMapper {

    public abstract Client toEntity(ClientDTO DTO);
    public abstract ClientResponseDTO toDTO(Client client);
}
