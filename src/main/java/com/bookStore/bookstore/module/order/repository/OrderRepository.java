package com.bookStore.bookstore.module.order.repository;

import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    boolean existsByBookAndStatusNot(Book book, StatusOrder status);
}
