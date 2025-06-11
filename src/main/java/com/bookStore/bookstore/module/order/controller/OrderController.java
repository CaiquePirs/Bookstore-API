package com.bookStore.bookstore.module.order.controller;

import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.service.OrderService;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController implements GenericController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderDTO dto){
        var orderResponse = service.create(dto);
        var uri = generateHeaderLocation(orderResponse.id());
        return ResponseEntity.created(uri).body(orderResponse);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Void> returnOrder(@PathVariable UUID id){
        service.returnedOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderResponseDTO> searchById(@PathVariable UUID id){
        var orderResponseDto = service.searchById(id);
        return ResponseEntity.ok(orderResponseDto);
    }

    @PutMapping("{id}")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid OrderDTO dto){
        var orderResponse = service.update(id, dto);
        var uri = generateHeaderLocation(id);
        return ResponseEntity.created(uri).body(orderResponse);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
