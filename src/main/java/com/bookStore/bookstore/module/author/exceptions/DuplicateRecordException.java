package com.bookStore.bookstore.module.author.exceptions;

public class DuplicateRecordException extends RuntimeException{
    public DuplicateRecordException(String message){
        super(message);
    }
}
