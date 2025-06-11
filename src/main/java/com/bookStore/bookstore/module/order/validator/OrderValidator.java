package com.bookStore.bookstore.module.order.validator;

import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.mapper.OrderMapper;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.user.exception.UserNotFoundException;
import com.bookStore.bookstore.module.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderValidator {

    private final BookService bookService;
    private final UserService userService;
    private final OrderMapper mapper;

    public Order validateOrder(OrderDTO dto){
        var book = bookService.getById(dto.bookId())
                .orElseThrow(() -> new BookNotFoundException(dto.bookId()));

        var user = userService.searchById(dto.userId())
                .orElseThrow(()-> new UserNotFoundException(dto.userId()));

        if (book.getStatus() == StatusBook.UNAVAILABLE) {
            throw new BookUnavailableException("this book is already loaned");
        }

        var order = mapper.toEntity(dto);
        order.setBook(book);
        order.setUser(user);
        order.setStatus(StatusOrder.LOANED);
        book.setStatus(StatusBook.UNAVAILABLE);
        return order;
    }
}
