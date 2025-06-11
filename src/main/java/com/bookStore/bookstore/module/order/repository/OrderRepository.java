package com.bookStore.bookstore.module.order.repository;

import com.bookStore.bookstore.module.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
