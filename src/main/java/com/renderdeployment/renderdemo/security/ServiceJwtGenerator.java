package com.renderdeployment.renderdemo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class ServiceJwtGenerator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiry}")
    private long expiry;

    public String generateToken() {

        return Jwts.builder()
                .setIssuer("order-service")
                .setSubject("service-auth")
                .claim("service", "ORDER_SERVICE")
                .claim("scope", "PAYMENT_CREATE")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiry))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}
