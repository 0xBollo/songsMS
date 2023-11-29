package htwb.ai.apigateway.util;

import htwb.ai.apigateway.exception.AuthorizationException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtUtils {

    public static String getUserIdFromToken(String signingKey, String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(signingKey.getBytes());

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            throw new AuthorizationException("Invalid Token");
        }
    }
}
