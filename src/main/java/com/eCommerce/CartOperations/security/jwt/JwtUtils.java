package com.eCommerce.CartOperations.security.jwt;

import com.eCommerce.CartOperations.security.user.EcomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.List;



@Component
public class JwtUtils {

    @Value("${jwt.expiration}")
    private int expirationTime;
    @Value("${jwt.secret}")
    private String jwtSecret;



    public String generateTokenForuser(Authentication authentication){
       EcomUserDetails userPrincipal= (EcomUserDetails) authentication.getPrincipal();
      List<String> roles= userPrincipal.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority).toList();


        return Jwts.builder()
                .subject(userPrincipal.getEmail())
                .claim("id", userPrincipal.getId())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey())
                .compact();

    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token){
        try {
            Claims claims = Jwts.parser()
                     .verifyWith(getKey())
                     .build()
                     .parseSignedClaims(token)
                     .getPayload();
            Date expiration =claims.getExpiration();
            return expiration.after(new Date());
        } catch (ExpiredJwtException e) {
            System.out.println("Token expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Token validation error: " + e.getMessage());
        }
return false;
}
}