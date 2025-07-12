package com.eCommerce.CartOperations.request;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Set<String> roles;

}
