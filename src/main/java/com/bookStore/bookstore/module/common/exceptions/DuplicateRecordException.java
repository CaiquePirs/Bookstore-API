package com.bookStore.bookstore.module.common.exceptions;

public class DuplicateRecordException extends RuntimeException{
    public DuplicateRecordException(String message){
        super(message);
    }
}
