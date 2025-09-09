package com.bookStore.bookstore.module.services;

import com.bookStore.bookstore.module.common.exceptions.NotFoundException;
import com.bookStore.bookstore.module.dtos.OrderRequestDTO;
import com.bookStore.bookstore.module.common.exceptions.OrderLoanedException;
import com.bookStore.bookstore.module.common.exceptions.OrderReturnedException;
import com.bookStore.bookstore.module.dtos.OrderUpdateDTO;
import com.bookStore.bookstore.module.entities.Order;
import com.bookStore.bookstore.module.enums.StatusBook;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.enums.StatusOrder;
import com.bookStore.bookstore.module.repositories.OrderRepository;
import com.bookStore.bookstore.module.validators.ClientValidator;
import com.bookStore.bookstore.module.validators.OrderValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderValidator validator;
    private final ClientAuditService clientAuditService;
    private final ClientValidator clientValidator;

    public Order create(OrderRequestDTO dto){
        Order order = validator.validateOrder(dto);

        clientValidator.validateIfIsTheSameClient(clientAuditService.getCurrentUserAuditId());
        clientValidator.validateIfIsTheSameClient(dto.clientId());

        order.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        return repository.save(order);
    }

    public Order findById(UUID orderId){
        return repository.findById(orderId)
                .filter(o -> !o.getStatusEntity().equals(StatusEntity.DELETED))
                .orElseThrow(() -> new NotFoundException("Order ID not found"));
    }

    public Page<Order> searchFilter(StatusOrder status, Integer page, Integer sizePage) {
        Specification<Order> specs = (root, query, cb) -> cb.conjunction();

        if (status != null) {
            specs = specs.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        Pageable pageRequest = PageRequest.of(page, sizePage);
        return repository.findAll(specs, pageRequest);
    }

    public void delete(UUID id){
        Order order = findById(id);

        if(order.getStatus().equals(StatusOrder.LOANED)){
            throw new OrderLoanedException("Error deleting: This order is active");
        }

        order.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        repository.deleteById(id);
    }

    public Order update(UUID orderId, OrderUpdateDTO dto){
        Order order = findById(orderId);

        if(order.getStatus() == StatusOrder.RETURNED){
            throw new OrderReturnedException("This order has already been returned");
        }

        order = validator.updateValidator(order, dto);
        order.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        return repository.save(order);
    }

    @Transactional
    public void returnedOrder(UUID orderId){
        Order order = findById(orderId);

        if(order.getStatus().equals(StatusOrder.RETURNED)){
            throw new OrderReturnedException("This order has already been returned");
        }

        clientValidator.validateIfIsTheSameClient(order.getClient().getId());

        order.setUserAuditId(clientAuditService.getCurrentUserAuditId());
        order.getBook().setStatus(StatusBook.AVAILABLE);
        order.setStatus(StatusOrder.RETURNED);
        repository.save(order);
    }
}
