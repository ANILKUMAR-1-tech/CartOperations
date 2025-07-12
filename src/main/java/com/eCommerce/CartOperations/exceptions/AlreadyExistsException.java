package com.eCommerce.CartOperations.exceptions;

public class AlreadyExistsException extends RuntimeException{
    public AlreadyExistsException(String s) {
        super(s);
    }
}
