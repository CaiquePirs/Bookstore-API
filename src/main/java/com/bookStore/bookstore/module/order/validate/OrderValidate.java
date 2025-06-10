package com.bookStore.bookstore.module.order.validate;

import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.mapper.OrderMapper;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.module.user.exception.UserNotFoundException;
import com.bookStore.bookstore.module.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderValidate {

    private final BookService bookService;
    private final UserService userService;
    private final OrderMapper mapper;
    private final OrderRepository repository;

    public Order validateOrder(OrderDTO dto){
        var bookId = bookService.getById(dto.bookId())
                .orElseThrow(() -> new BookNotFoundException(dto.bookId()));

        var userId = userService.searchById(dto.userId())
                .orElseThrow(()-> new UserNotFoundException(dto.userId()));

        if(repository.existsByBookId(dto.bookId())){
            throw new BookUnavailableException("this book is already loaned");
        }

        var order = mapper.toEntity(dto);
        order.setBook(bookId);
        order.setUser(userId);
        return  order;

    }
}
