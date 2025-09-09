package com.bookStore.bookstore.module.controllers;

import com.bookStore.bookstore.docs.OrderApi;
import com.bookStore.bookstore.module.dtos.OrderRequestDTO;
import com.bookStore.bookstore.module.dtos.OrderResponseDTO;
import com.bookStore.bookstore.module.dtos.OrderUpdateDTO;
import com.bookStore.bookstore.module.entities.Order;
import com.bookStore.bookstore.module.enums.StatusOrder;
import com.bookStore.bookstore.module.mappers.OrderMapper;
import com.bookStore.bookstore.module.services.OrderService;
import com.bookStore.bookstore.module.utils.UtilsMethods;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService service;
    private final UtilsMethods generic;
    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO dto){
        Order order = service.create(dto);
        URI uri = generic.generateHeaderLocation(order.getId());
        return ResponseEntity.created(uri).body(orderMapper.toDTO(order));
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> returnOrder(@PathVariable UUID id){
        service.returnedOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> findOrderById(@PathVariable UUID id){
        Order order = service.findById(id);
        return ResponseEntity.ok(orderMapper.toDTO(order));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderResponseDTO>> filterSearch(
            @RequestParam(value = "status", required = false) StatusOrder status,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size-page", defaultValue = "10") Integer sizePage) {

        Page<Order> pageResult = service.searchFilter(status, page, sizePage);
        Page<OrderResponseDTO> result = pageResult.map(orderMapper::toDTO);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable UUID id, @RequestBody OrderUpdateDTO dto){
        Order order = service.update(id, dto);
        URI uri = generic.generateHeaderLocation(id);
        return ResponseEntity.created(uri).body(orderMapper.toDTO(order));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
