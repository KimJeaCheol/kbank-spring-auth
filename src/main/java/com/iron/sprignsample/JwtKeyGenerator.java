package com.iron.sprignsample;

import java.util.Base64;

import javax.crypto.SecretKey;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // or HS256, HS384
        String base64EncodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64EncodedKey);
    }
}