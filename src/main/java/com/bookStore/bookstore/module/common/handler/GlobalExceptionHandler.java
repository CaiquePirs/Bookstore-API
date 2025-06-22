package com.bookStore.bookstore.module.common.handler;

import com.bookStore.bookstore.module.author.exception.AuthorDeletedException;
import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.common.error.ErrorField;
import com.bookStore.bookstore.module.common.error.ErrorResponseDTO;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.order.exception.OrderLoanedException;
import com.bookStore.bookstore.module.order.exception.OrderNotFoundException;
import com.bookStore.bookstore.module.order.exception.OrderReturnedException;
import com.bookStore.bookstore.module.client.exception.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDTO> buildNotFoundResponse(String field, String message) {
        ErrorField errorField = new ErrorField(field, message);
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                message,
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponseDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ErrorField> listErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error",
                listErrors
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ErrorResponseDTO> handleDateTimeParseException(DateTimeParseException e) {
        ErrorField errorField = new ErrorField("date", "Invalid date format. Use default: yyyy-MM-dd.");
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Error converting date",
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(AuthorDeletedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorDeleted(AuthorDeletedException e) {
        ErrorField errorField = new ErrorField("Author", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorNotFound(AuthorNotFoundException e) {
        ErrorField errorField = new ErrorField("Author", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookNotFound(BookNotFoundException e){
        ErrorField errorField = new ErrorField("Book", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleClientNotFound(ClientNotFoundException e){
        ErrorField errorField = new ErrorField("Client", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderNotFound(OrderNotFoundException e){
        ErrorField errorField = new ErrorField("Order", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<ErrorResponseDTO> handleBookUnavailable(BookUnavailableException e) {
        ErrorField errorField = new ErrorField("Book", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(OrderLoanedException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderLoaned(OrderLoanedException e){
        ErrorField errorField = new ErrorField("Order", e.getMessage());
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponseDTO);
    }

    @ExceptionHandler(OrderReturnedException.class)
    public ResponseEntity<ErrorResponseDTO> handleOrderReturned(OrderReturnedException e){
        ErrorField errorField = new ErrorField("Order", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateRecordException(DuplicateRecordException e){
        ErrorField errorField = new ErrorField("Duplicate", e.getMessage());
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                e.getMessage(),
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(NoHandlerFoundException e) {
        return buildNotFoundResponse("Url", "Url not found");
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        ErrorField errorField = new ErrorField("URL", "Method Not Allowed");
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.METHOD_NOT_ALLOWED.value(),
                "Method not allowed for this endpoint.",
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorField errorField = new ErrorField("Error", "Invalid data type");
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Error: Invalid data type",
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleInternalError(RuntimeException e) {
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                List.of(new ErrorField("Exception", e.getClass().getSimpleName()))
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleAccessDeniedException(AccessDeniedException e){
      return new ErrorResponseDTO(HttpStatus.FORBIDDEN.value(), "Access denied", List.of());
    }
}
