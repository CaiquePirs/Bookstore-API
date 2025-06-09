package com.bookStore.bookstore.module.book.repository;

import com.bookStore.bookstore.module.book.model.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> isbnEqual(String isbn) {
        return (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
    }

    public static Specification<Book> titleLike(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> publisherEqual(String publisher) {
        return (root, query, cb) -> cb.equal(root.get("publisher"), publisher);
    }

    public static Specification<Book> authorNameLike(String authorName) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.join("author").get("name")), "%" + authorName.toLowerCase() + "%");
    }
}

