package htwb.ai.authservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("WcQhbn6#^qdD5uG#bPX#7gt&Ef54G8$%H^Y&vC3r".getBytes());

    public static String generateToken(String userId, long validity) {
        long now = System.currentTimeMillis();
        long expiration = now + validity;

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}