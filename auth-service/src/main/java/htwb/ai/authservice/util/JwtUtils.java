package htwb.ai.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    public static String generateToken(String signingKey, String userId, long validity) {
        SecretKey secretKey = Keys.hmacShaKeyFor(signingKey.getBytes());
        long now = System.currentTimeMillis();
        long expiration = now + validity;

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}