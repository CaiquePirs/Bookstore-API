package com.bookStore.bookstore.module.order.validator;

import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.mapper.OrderMapper;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderValidator {

    private final BookService bookService;
    private final ClientService clientService;
    private final OrderMapper mapper;

    public Order validateOrder(OrderDTO dto){
        var book = bookService.getById(dto.bookId());
        var client = clientService.searchById(dto.clientId());

        if (book.getStatus() == StatusBook.UNAVAILABLE) {
            throw new BookUnavailableException("this book is already loaned");
        }

        var order = mapper.toEntity(dto);
        order.setBook(book);
        order.setClient(client);
        order.setStatus(StatusOrder.LOANED);
        book.setStatus(StatusBook.UNAVAILABLE);
        return order;
    }
}
