package beertech.becks.api.security.service;

import beertech.becks.api.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtService {
    @Value("${security.jwt.expiry}")
    private String expiry;

    @Value("${security.jwt.key}")
    private String key;

    public String getToken(User user) {
        LocalDateTime dateTimeExpiry = LocalDateTime.now().plusMinutes(Long.parseLong(expiry));
        return Jwts.builder().setClaims(getClaimsUser(user)).
                setSubject(user.getEmail())
                .setExpiration(Date.from(dateTimeExpiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    public boolean isValidToken(String token) {
        try {
            Claims claims = getClaims(token);
            return LocalDateTime.now()
                    .isBefore(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserLogin(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

    private Claims getClaimsUser(User user) {
		Claims claimsUser = Jwts.claims().setSubject(user.getEmail());
		claimsUser.put("id", user.getId());
		claimsUser.put("documentNumber", user.getDocumentNumber());
		claimsUser.put("auth", user.getRole());
		claimsUser.put("name", user.getName());
    	return claimsUser;
	}
}