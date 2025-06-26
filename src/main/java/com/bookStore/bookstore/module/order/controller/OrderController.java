package com.bookStore.bookstore.module.order.controller;

import com.bookStore.bookstore.docs.OrderControllerDoc;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.order.service.OrderService;
import com.bookStore.bookstore.module.order.util.GenerateOrderResponse;
import com.bookStore.bookstore.module.util.GenericController;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController implements GenericController, OrderControllerDoc {

    private final OrderService service;
    private final GenerateOrderResponse generate;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponseDTO> create(@RequestBody @Valid OrderDTO dto){
        var orderResponse = service.create(dto);
        var uri = generateHeaderLocation(orderResponse.id());
        return ResponseEntity.created(uri).body(orderResponse);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> returnOrder(@PathVariable UUID id){
        service.returnedOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> searchById(@PathVariable UUID id){
        var orderResponseDto = service.searchById(id);
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> filterSearch(
            @RequestParam(value = "status", required = false) StatusOrder status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage) {

        Page<Order> pageResult = service.searchFilter(status, page, sizePage);
        Page<OrderResponseDTO> result = pageResult.map(generate::createOrderResponseDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> update(@PathVariable UUID id, @RequestBody @Valid OrderDTO dto){
        var orderResponse = service.update(id, dto);
        var uri = generateHeaderLocation(id);
        return ResponseEntity.created(uri).body(orderResponse);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
