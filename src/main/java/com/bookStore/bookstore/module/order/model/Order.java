package com.bookStore.bookstore.module.order.model;

import com.bookStore.bookstore.module.user.model.User;
import com.bookStore.bookstore.module.book.model.Book;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private boolean status = true;

    @OneToOne
    @JoinColumn(name = "bookId")
    private Book book;

    @OneToOne
    @JoinColumn(name = "userId")
    private User user;

    @CreationTimestamp
    private LocalDateTime creationTimestamp;

    @UpdateTimestamp
    private LocalDateTime updateTimeStamp;

    public String statusOrder(){
        return status ? "Loaned" : "Returned";
    }

    public Order(){}

    public Order(boolean status, Book book, User user){
        this.status = status;
        this.book = book;
        this.user = user;
    }

}
