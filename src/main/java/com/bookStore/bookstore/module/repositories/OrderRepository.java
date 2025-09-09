package com.bookStore.bookstore.module.repositories;

import com.bookStore.bookstore.module.entities.Book;
import com.bookStore.bookstore.module.entities.Order;
import com.bookStore.bookstore.module.enums.StatusOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order> {
    boolean existsByBookAndStatusNot(Book book, StatusOrder status);
}
