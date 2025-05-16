package com.bbva.exchange.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JwtUtil {

    public static String generateToken(UUID userId, String username, List<String> roles) {
        System.out.println("Generating token for user: " + username);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId.toString())
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setIssuer(AppConstants.JWT_ISSUER)
                .signWith(Keys.hmacShaKeyFor(AppConstants.JWT_SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(AppConstants.JWT_SECRET_KEY.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    public static String extractUsername(String token) {
        return validateToken(token).getSubject();
    }

    public static List<String> extractRoles(String token) {
        Claims claims = validateToken(token);
        return (List<String>) claims.get("roles", List.class);
    }

    public static UUID extractUserId(String token) {
        Claims claims = validateToken(token);
        return UUID.fromString(claims.get("userId", String.class));
    }
}