package com.tms.TaskManagementSystem.services.impl;

import com.tms.TaskManagementSystem.entity.Worker;
import com.tms.TaskManagementSystem.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final String SECRET_KEY = "c4c25bff69b2c46050cb9f409d01ec908fd71e20176be2a5d6e97580e15a9b2e";

    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean isValid(String token, UserDetails worker)
    {
        String username = extractUsername(token);
        return (username.equals(worker.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver)
    {
        Claims claims = extractClaims(token);
        return resolver.apply(claims);
    }

    @Override
    public String generateToken(Worker worker) {

        return Jwts.builder()
                .subject(worker.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24*60*60*1000))
                .signWith(getSigningKey(SECRET_KEY))
                .compact();
    }

    @Override
    public SecretKey getSigningKey(String key) {
        byte[] keyBytes = Decoders.BASE64URL.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Claims extractClaims(String token) {
        return Jwts.
                parser()
                .verifyWith(getSigningKey(SECRET_KEY))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

