package com.bookStore.bookstore.module.order.service;

import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.service.BookService;
import com.bookStore.bookstore.module.order.DTO.OrderDTO;
import com.bookStore.bookstore.module.order.DTO.OrderResponseDTO;
import com.bookStore.bookstore.module.order.exception.OrderLoanedException;
import com.bookStore.bookstore.module.order.exception.OrderNotFoundException;
import com.bookStore.bookstore.module.order.exception.OrderReturnedException;
import com.bookStore.bookstore.module.order.model.Order;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.module.order.util.GenerateOrderResponse;
import com.bookStore.bookstore.module.order.validator.OrderValidator;
import com.bookStore.bookstore.module.client.service.ClientService;
import com.bookStore.bookstore.security.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;
    private final OrderValidator validate;
    private final BookService bookService;
    private final ClientService clientService;
    private final GenerateOrderResponse generate;
    private final SecurityService securityService;

    public OrderResponseDTO create(OrderDTO dto){
        var order = validate.validateOrder(dto);

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        order.setUserAuditId(findUserLogged.getId());

        repository.save(order);
        return generate.createOrderResponseDTO(order);
    }

    public OrderResponseDTO searchById(UUID id){
        var order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order ID not found"));
        return generate.createOrderResponseDTO(order);
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
      var order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order ID not found"));

        if(order.getStatus().equals(StatusOrder.LOANED)){
            throw new OrderLoanedException("Error deleting: This order is active");
        }

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        order.setUserAuditId(findUserLogged.getId());

        repository.deleteById(id);
    }

    public OrderResponseDTO update(UUID id, OrderDTO dto){
        var existingOrder = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order ID not found"));

        if(existingOrder.getStatus() == StatusOrder.RETURNED){
            throw new OrderReturnedException("This order has already been returned");
        }

        if (dto.clientId() != null) {
            var client = clientService.searchById(dto.clientId());
            existingOrder.setClient(client);
        }

        if (dto.bookId() != null) {
            var book = bookService.getById(dto.bookId());
            existingOrder.setBook(book);
        }

        if (dto.statusBook() != null) {
            existingOrder.getBook().setStatus(dto.statusBook());
        }

        if (dto.statusOrder() != null) {
            existingOrder.setStatus(dto.statusOrder());
        }

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        existingOrder.setUserAuditId(findUserLogged.getId());

        repository.save(existingOrder);
        return generate.createOrderResponseDTO(existingOrder);
    }

    public void returnedOrder(UUID id){
        var order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order ID not found"));

        if(order.getStatus().equals(StatusOrder.RETURNED)){
            throw new OrderReturnedException("This order has already been returned");
        }

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        order.setUserAuditId(findUserLogged.getId());

        order.getBook().setStatus(StatusBook.AVAILABLE);
        order.setStatus(StatusOrder.RETURNED);
        repository.save(order);
    }
}
