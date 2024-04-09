package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.entity.Worker;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

public interface JwtService {
    public String generateToken(Worker worker);
    public SecretKey getSigningKey(String key);
    public Claims extractClaims(String token);
    public String extractUsername(String token);
    public boolean isValid(String token, UserDetails worker);
    public boolean isTokenExpired(String token);
    public Date extractExpiration(String token);
    public <T> T extractClaim(String token, Function<Claims, T> resolver);
}
