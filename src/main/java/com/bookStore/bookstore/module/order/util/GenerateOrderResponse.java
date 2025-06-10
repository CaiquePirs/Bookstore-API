package com.bookStore.bookstore.module.order.util;

import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class GenerateOrderResponse {

    public OrderResponseDTO createOrderResponseDTO(Order order){
        OrderResponseDTO dto = new OrderResponseDTO(
                order.getId(),
                order.getBook().getTitle(),
                order.getUser().getUsername(),
                order.getUser().getEmail(),
                order.getCreationTimestamp(),
                order.statusOrder());
        return dto;
    }
}
