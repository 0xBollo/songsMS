package htwb.ai.songservice.util;

import htwb.ai.songservice.exception.AuthorizationException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtils {

    private static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("WcQhbn6#^qdD5uG#bPX#7gt&Ef54G8$%H^Y&vC3r".getBytes());

    public static String getUserIdFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new AuthorizationException("Invalid Token");
        }
    }
}