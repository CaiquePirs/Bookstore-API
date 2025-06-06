package com.bookStore.bookstore.module.author.controller.common;

import com.bookStore.bookstore.module.author.DTO.ErrorField;
import com.bookStore.bookstore.module.author.DTO.ErrorResponse;
import com.bookStore.bookstore.module.author.exceptions.AuthorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        List<ErrorField> listErrors = fieldErrors
                .stream()
                .map(fe -> new ErrorField(fe.getField(), fe.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", listErrors);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ErrorResponse handleDateTimeParseException(DateTimeParseException e) {

        ErrorField errorField = new ErrorField("date", "Invalid date format. Use default: yyyy-MM-dd.");
        return new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Error converting date", List.of(errorField));
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthorNotFoundException(AuthorNotFoundException e){

        ErrorField errorField = new ErrorField("Id", "Author not found");
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "ID author not found", List.of(errorField));

    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoHandlerFoundException e) {

        ErrorField errorField = new ErrorField("Url", "URL not found");
        ErrorResponse error = ErrorResponse.notFound(errorField.error());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handlerMethodNotSupported(HttpRequestMethodNotSupportedException e){

        ErrorField errorField = new ErrorField("URL", "Method Not Allowed");

        ErrorResponse error = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Method not allowed for this endpoint.", List.of(errorField));
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(error);

    }

}
