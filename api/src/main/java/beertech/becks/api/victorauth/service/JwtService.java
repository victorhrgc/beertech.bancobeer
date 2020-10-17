package beertech.becks.api.victorauth.service;

import beertech.becks.api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * A service which does some steps of the JWT authentication
 *
 * @author victorhrgc
 */
@Service
public class JwtService {
    @Value("${security.jwt.expiry}")
    private String expiry;

    @Value("${security.jwt.key}")
    private String key;

    /**
     * Builds a JWT Token from a user
     *
     * @param user the user requesting the token
     * @return a string which is the jwt token
     */
    public String getToken(User user) {
        LocalDateTime dateTimeExpiry = LocalDateTime.now().plusMinutes(Long.parseLong(expiry));

        return Jwts.builder()
                //.setSubject(user.getLogin())
                .setExpiration(Date.from(dateTimeExpiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * A method that checks if a given token is valid
     *
     * @param token the token to be checked
     * @return true if the token is valid, false otherwise
     */
    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaims(token);
            return LocalDateTime.now().isBefore(claims.getExpiration()
                    .toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the user login from the token
     *
     * @return the user bearing the token
     */
    public String getUserLogin(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Gets the claims from a token
     *
     * @param token the token
     * @return the claims
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }
}