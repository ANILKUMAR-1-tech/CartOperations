package com.eCommerce.CartOperations.security.jwt;

import java.util.Base64;
import javax.crypto.KeyGenerator;

public class JwtKeyGen {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
        keyGen.init(256);
        byte[] encoded = keyGen.generateKey().getEncoded();
        System.out.println(Base64.getEncoder().encodeToString(encoded));
    }
}
