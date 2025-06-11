package com.bookStore.bookstore.module.order.service;

import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.exception.OrderNotFoundException;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.module.order.util.GenerateOrderResponse;
import com.bookStore.bookstore.module.order.validator.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderValidator validate;
    private final GenerateOrderResponse generate;

    public OrderResponseDTO create(OrderDTO dto){
        var order = validate.validateOrder(dto);
        repository.save(order);
        return generate.createOrderResponseDTO(order);
    }

    public OrderResponseDTO searchById(UUID id){
        var order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        return generate.createOrderResponseDTO(order);
    }

    public void delete(UUID id){
      var order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if(order.getStatus().equals(StatusOrder.LOANED)){
            throw new OrderLoanedException();
        }
        repository.deleteById(id);
    }

}
