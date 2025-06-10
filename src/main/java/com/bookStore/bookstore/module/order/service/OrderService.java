package com.bookStore.bookstore.module.order.service;

import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.module.order.validate.OrderValidate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderValidate validate;

    public OrderResponseDTO create(OrderDTO dto){
        var order = validate.validateOrder(dto);
        repository.save(order);

        OrderResponseDTO orderResponse = new OrderResponseDTO(
                order.getId(),
                order.getBook().getTitle(),
                order.getUser().getUsername(),
                order.getUser().getEmail(),
                order.getCreationTimestamp(),
                order.statusOrder());
        return orderResponse;
    }
}
