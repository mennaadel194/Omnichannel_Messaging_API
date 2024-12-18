package com.omnichannel.messagingplatform.security;

import com.omnichannel.messagingplatform.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;  // Secret key for signing the JWT token (should be in application.properties)

    @Value("${jwt.expiration}")
    private long expirationTime; // Token expiration time in milliseconds

    // Method to generate a JWT token for a user
    public String generateToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("username", user.getUsername());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Method to extract the username from the JWT token
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();  // The username is stored in the "sub" claim
    }

    public Long extractUserId(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.parseLong(claims.getSubject());
    }

    // Method to extract claims from the token
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Method to validate the token
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);  // This will throw an exception if the token is invalid
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
