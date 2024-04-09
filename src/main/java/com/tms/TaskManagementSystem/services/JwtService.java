package com.tms.TaskManagementSystem.services;

import com.tms.TaskManagementSystem.entity.Worker;
import io.jsonwebtoken.Claims;

import javax.crypto.SecretKey;

public interface JwtService {
    public String generateToken(Worker worker);
    public SecretKey getSigningKey(String key);
    public Claims extractClaims(String token);
}
