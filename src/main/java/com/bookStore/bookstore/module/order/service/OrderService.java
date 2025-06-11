package com.bookStore.bookstore.module.order.service;

import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.exception.OrderLoanedException;
import com.bookStore.bookstore.module.order.exception.OrderNotFoundException;
import com.bookStore.bookstore.module.order.exception.OrderReturnedException;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.module.order.util.GenerateOrderResponse;
import com.bookStore.bookstore.module.order.validator.OrderValidator;
import com.bookStore.bookstore.module.user.exception.UserNotFoundException;
import com.bookStore.bookstore.module.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderValidator validate;
    private final BookService bookService;
    private final UserService userService;
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

    public OrderResponseDTO update(UUID id, OrderDTO dto){
        var existingOrder = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if(existingOrder.getStatus() == StatusOrder.RETURNED){
            throw new OrderReturnedException();
        }

        if (dto.userId() != null) {
            var user = userService.searchById(dto.userId())
                    .orElseThrow(() -> new UserNotFoundException(dto.userId()));
            existingOrder.setUser(user);
        }

        if (dto.bookId() != null) {
            var book = bookService.getById(dto.bookId())
                    .orElseThrow(() -> new BookNotFoundException(dto.bookId()));
            existingOrder.setBook(book);
        }

        if (dto.statusBook() != null) {
            existingOrder.getBook().setStatus(dto.statusBook());
        }

        if (dto.statusOrder() != null) {
            existingOrder.setStatus(dto.statusOrder());
        }

        repository.save(existingOrder);
        return generate.createOrderResponseDTO(existingOrder);
    }
}
