package com.example.bbapp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtJavaProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //Generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expireDate)
                .signWith(key())
                .compact();

        return token;

    }

    private SecretKey key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }


    //Get username from JWT token
    public String getUsername(String token){


        Claims claims = Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();

        return username;
    }

    //validate JWT token
    public boolean validateToken(String token){
        Jwts.parser()
                .verifyWith(key())
                .build()
                .parse(token);

        return true;
    }
}
