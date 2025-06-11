package com.bookStore.bookstore.module.common.handler;

import com.bookStore.bookstore.module.book.exception.BookNotFoundException;
import com.bookStore.bookstore.module.book.exception.BookUnavailableException;
import com.bookStore.bookstore.module.common.error.ErrorField;
import com.bookStore.bookstore.module.common.error.ErrorResponse;
import com.bookStore.bookstore.module.author.exception.AuthorNotFoundException;
import com.bookStore.bookstore.module.common.exception.DuplicateRecordException;
import com.bookStore.bookstore.module.order.exception.OrderLoanedException;
import com.bookStore.bookstore.module.order.exception.OrderNotFoundException;
import com.bookStore.bookstore.module.order.exception.OrderReturnedException;
import com.bookStore.bookstore.module.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
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

    private ResponseEntity<ErrorResponse> buildNotFoundResponse(String field, String message) {
        ErrorField errorField = new ErrorField(field, message);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                message,
                List.of(errorField)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<ErrorField> listErrors = fieldErrors
                .stream()
                .map(fe -> new ErrorField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Validation error", listErrors);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException e) {
        ErrorField errorField = new ErrorField("date", "Invalid date format. Use default: yyyy-MM-dd.");
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Error converting date", List.of(errorField));
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException e) {
        return buildNotFoundResponse("Id", "Book not found");
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorNotFound(AuthorNotFoundException e) {
        return buildNotFoundResponse("Id", "Author not found");
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException e) {
        return buildNotFoundResponse("Id", "User not found");
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerOrderNotFound(OrderNotFoundException e){
        return buildNotFoundResponse("Id", "Order not found");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException e) {
        return buildNotFoundResponse("Url", "Url not found");
    }

    @ExceptionHandler(BookUnavailableException.class)
    public ResponseEntity<ErrorResponse> handlerBookUnavaiable(BookUnavailableException e){
        return buildNotFoundResponse("Book", "This book is already loaned");
    }

    @ExceptionHandler(OrderLoanedException.class)
    public ResponseEntity<ErrorResponse> handlerOrderLoaned(OrderLoanedException e){
        return buildNotFoundResponse("Error", "Error deleting: This order is active");
    }

    @ExceptionHandler(OrderReturnedException.class)
    public ResponseEntity<ErrorResponse> handlerOrderLoaned(OrderReturnedException e){
        return buildNotFoundResponse("Error", "This order has already been returned");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerMethodNotSupported(HttpRequestMethodNotSupportedException e){

        ErrorField errorField = new ErrorField("URL", "Method Not Allowed");
        ErrorResponse error = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not allowed for this endpoint.", List.of(errorField));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlerMessageNotReadableException(HttpMessageNotReadableException e){
      ErrorField errorField = new ErrorField("Error", "Invalid data type");
      ErrorResponse error = ErrorResponse.standardResponse("Error Invalid data type ");
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handlerDuplicateRecordException(DuplicateRecordException e){
        return ErrorResponse.conflict(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handlerInternalError(RuntimeException e){
        return new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred",
                List.of()
        );
    }
}
