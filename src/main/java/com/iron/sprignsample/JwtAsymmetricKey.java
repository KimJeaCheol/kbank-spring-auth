package com.iron.sprignsample;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtAsymmetricKey {
    public static void main(String[] args) {
        // RSA 키 쌍 생성
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        System.out.println("privateKey : " + privateKey);
        System.out.println("publicKey : " + publicKey);
        // JWT 생성 및 서명 (개인 키 사용)
        // String jwt = Jwts.builder()
        //         .setSubject("user@example.com")
        //         .setExpiration(new Date(System.currentTimeMillis() + 3600000))
        //         .signWith(privateKey)
        //         .compact();

        // System.out.println("Generated JWT: " + jwt);

        // // JWT 검증 (공개 키 사용)
        // Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(jwt);
        // System.out.println("JWT is valid");
    }
}
