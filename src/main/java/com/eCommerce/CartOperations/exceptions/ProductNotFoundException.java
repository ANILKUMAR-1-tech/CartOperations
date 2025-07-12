package com.eCommerce.CartOperations.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message)
    {
        super(message);
    }
}
