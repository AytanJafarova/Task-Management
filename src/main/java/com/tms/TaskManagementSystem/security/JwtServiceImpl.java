package com.tms.TaskManagementSystem.security;

import com.tms.TaskManagementSystem.entity.Worker;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Transactional
public class JwtServiceImpl implements JwtService {

    @Value("${spring.jwt.secret_key}")
    private String SECRET_KEY;
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
                .expiration(new Date(System.currentTimeMillis() + 2*60*60*1000))
                .signWith(getSigningKey(SECRET_KEY))
                .compact();
    }
}