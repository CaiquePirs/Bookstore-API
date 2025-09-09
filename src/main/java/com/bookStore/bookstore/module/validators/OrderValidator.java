package com.bookStore.bookstore.module.validators;

import com.bookStore.bookstore.module.common.exceptions.BookUnavailableException;
import com.bookStore.bookstore.module.dtos.OrderUpdateDTO;
import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.repositories.OrderRepository;
import com.bookStore.bookstore.module.services.BookService;
import com.bookStore.bookstore.module.dtos.OrderRequestDTO;
import com.bookStore.bookstore.module.mappers.OrderMapper;
import com.bookStore.bookstore.module.entities.Order;
import com.bookStore.bookstore.module.enums.StatusOrder;
import com.bookStore.bookstore.module.services.ClientAuditService;
import com.bookStore.bookstore.module.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@AllArgsConstructor
public class OrderValidator {

    private final BookService bookService;
    private final ClientService clientService;
    private final OrderMapper mapper;
    private final OrderRepository orderRepository;
    private final ClientAuditService clientAuditService;

    public Order validateOrder(OrderRequestDTO dto){
        Book book = bookService.findById(dto.bookId());
        Client client = clientService.findById(dto.clientId());

        if (book.getStatus() == StatusBook.UNAVAILABLE) {
            throw new BookUnavailableException("this book is already loaned");
        }

        Order order = mapper.toEntity(dto);
        book.setStatus(StatusBook.UNAVAILABLE);
        order.setBook(book);
        order.setClient(client);
        order.setStatus(StatusOrder.LOANED);
        return order;
    }

    public void validateIfExistOrderActive(Book book){
        boolean existOrderActive = orderRepository.existsByBookAndStatusNot(book, StatusOrder.RETURNED);
        if (existOrderActive){
            throw new BookUnavailableException("This book cannot be deleted because it has an active order");
        }
    }

    public Order updateValidator(Order order, OrderUpdateDTO dto) {
        if (dto.clientId() != null) {
            Client client = clientService.findById(dto.clientId());
            order.setClient(client);
        }
        if (dto.bookId() != null) {
            Book book = bookService.findById(dto.bookId());
            order.setBook(book);
        }
        if (dto.statusBook() != null) {
            order.getBook().setStatus(dto.statusBook());
        }
        if (dto.statusOrder() != null) {
            order.setStatus(dto.statusOrder());
        }
        return order;
    }


}
