package com.bookStore.bookstore.module.mappers;

import com.bookStore.bookstore.module.dtos.OrderRequestDTO;
import com.bookStore.bookstore.module.dtos.OrderResponseDTO;
import com.bookStore.bookstore.module.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toEntity(OrderRequestDTO dto);

    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "clientName", source = "client.username")
    @Mapping(target = "clientEmail", source = "client.email")
    OrderResponseDTO toDTO(Order order);
}
