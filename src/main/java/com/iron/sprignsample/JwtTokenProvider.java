package com.iron.sprignsample;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private int jwtExpirationInMs;

    private Key key;

    
    @PostConstruct
    protected void init() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String ci, TransactionInfo transactionInfo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "KBANK");
        claims.put("sub", ci);
        claims.put("aud", "di");
        claims.put("nbf", System.currentTimeMillis() / 1000);  // Not Before (in seconds)
        claims.put("jti", UUID.randomUUID().toString());  // JWT ID
        claims.put("transactionId", transactionInfo.getTransactionId());
        claims.put("securityName", transactionInfo.getSecurityName());
        claims.put("amount", transactionInfo.getAmount());
        claims.put("transactionType", transactionInfo.getTransactionType());

        Date now = new Date();
        // Date expiryDate = Date.from(Instant.now().plus(jwtExpirationInMs, ChronoUnit.MILLIS));
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512) //서명 해싱하기 위한 알고리즘 지정
                .compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            // handle specific exception
            return false;
        }
        // handle other specific exceptions
        
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
