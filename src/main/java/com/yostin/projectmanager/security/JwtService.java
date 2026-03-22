package com.yostin.projectmanager.security;

import java.security.Key;
import java.sql.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.yostin.projectmanager.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("name", user.getName())
                .claim("id", user.getId())
                .issuedAt(new Date(System.currentTimeMillis())) // COMENTAR DE ARGUMENTO
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();

    }

    // 🧠 Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !claims.getExpiration().before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            return false;
        }
    }
}
