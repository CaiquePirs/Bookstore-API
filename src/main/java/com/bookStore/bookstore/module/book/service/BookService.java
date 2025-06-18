package com.bookStore.bookstore.module.book.service;

import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.book.model.StatusBook;
import com.bookStore.bookstore.module.book.repository.BookSpecs;
import com.bookStore.bookstore.module.author.service.AuthorService;
import com.bookStore.bookstore.module.book.DTO.BookDTO;
import com.bookStore.bookstore.module.book.mapper.BookMapper;
import com.bookStore.bookstore.module.book.model.Book;
import com.bookStore.bookstore.module.book.repository.BookRepository;
import com.bookStore.bookstore.module.book.validator.BookValidator;
import com.bookStore.bookstore.module.client.service.ClientService;
import com.bookStore.bookstore.module.order.model.StatusOrder;
import com.bookStore.bookstore.module.order.repository.OrderRepository;
import com.bookStore.bookstore.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository repository;
    private final OrderRepository orderRepository;
    private final BookValidator validator;
    private final BookMapper mapper;
    private final AuthorService authorService;
    private final SecurityService securityService;
    private final ClientService clientService;

    public Book create(BookDTO dto){
        var author = authorService.searchById(dto.authorId());

        validator.validateIsbn(dto.isbn(), null);
        Book book = mapper.toEntity(dto);

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        book.setUserAuditId(findUserLogged.getId());

        book.setAuthor(author);
        book.setStatus(StatusBook.AVAILABLE);
        return repository.save(book);
    }

    public Book getById(UUID id) {
        return repository.findById(id)
                .map((Book book) -> {
                    if (book.getStatus().equals(StatusBook.DELETED_AT)) {
                        throw new BookUnavailableException("This book is deleted in the system");
                    }
                    return book;
                })
                .orElseThrow(() -> new BookNotFoundException("Book ID not found"));
    }

    public Page<Book> searchBooksByQuery(String title,
                                         String isbn,
                                         String publisher,
                                         String author,
                                         StatusBook status,
                                         Integer page,
                                         Integer sizePage) {

        Specification<Book> specs = (root, query, cb) -> cb.conjunction(); // true

        if (isbn != null && !isbn.isBlank()) {
            specs = specs.and(BookSpecs.isbnEqual(isbn));
        }

        if (title != null && !title.isBlank()) {
            specs = specs.and(BookSpecs.titleLike(title));
        }

        if (publisher != null && !publisher.isBlank()) {
            specs = specs.and(BookSpecs.publisherEqual(publisher));
        }

        if (author != null && !author.isBlank()) {
            specs = specs.and(BookSpecs.authorNameLike(author));
        }

        if (status != null) {
            specs = specs.and((root, query, cb) -> cb.equal(root.get("status"), status));
        } else {
            specs = specs.and((root, query, cb) -> cb.notEqual(root.get("status"), StatusBook.DELETED_AT));
        }

        Pageable pageRequest = PageRequest.of(page, sizePage);
        return repository.findAll(specs, pageRequest);
    }


    public void deleteById(UUID id){
        var existBook = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book ID not found"));

        var existOrderActive = orderRepository.existsByBookAndStatusNot(existBook, StatusOrder.RETURNED);

        if (existOrderActive) {
            throw new BookUnavailableException("This book cannot be deleted because it has an active order");
        }

        if(existBook.getStatus().equals(StatusBook.DELETED_AT)){
            throw new BookUnavailableException("This book has already been deleted");
        }

        existBook.setStatus(StatusBook.DELETED_AT);

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        existBook.setUserAuditId(findUserLogged.getId());

        repository.save(existBook);
    }

    public void searchISBN(String isbn, UUID bookId){
        validator.validateIsbn(isbn, bookId);
    }

    public Book update(UUID id, BookDTO dto){
        searchISBN(dto.isbn(), id);

        var book = repository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book ID not found"));

        if(book.getStatus().equals(StatusBook.DELETED_AT)){
            throw new BookUnavailableException("This book has already been deleted");
        }

        var author = authorService.searchById(dto.authorId());

        var userLogged = securityService.getLoggedUsername();
        var findUserLogged = clientService.getClientForAudit(userLogged);
        book.setUserAuditId(findUserLogged.getId());

        book.setIsbn(dto.isbn());
        book.setTitle(dto.title());
        book.setPublisher(dto.publisher());
        book.setPublicationDate(dto.publicationDate());
        book.setAuthor(author);

        return repository.save(book);
    }

}
