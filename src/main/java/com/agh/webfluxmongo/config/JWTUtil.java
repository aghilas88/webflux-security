package com.agh.webfluxmongo.config;

import com.agh.webfluxmongo.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {

    private String secret = "azertyuiopqsdfghjklmwxcvbn1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
    private long expireTimeInMiliSec = 120000L;

     public String generateToken(User user) {
         Date now = new Date();
         Map<String, Object> cleam = new HashMap<>();
         cleam.put("alg", "HS256");
         cleam.put("type", "JWT");

         return Jwts.builder()
                 .setSubject(user.getUsername())
                 .setIssuedAt(now)
                 .setExpiration(new Date(now.getTime() + expireTimeInMiliSec))
                 .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                 .setHeaderParams(cleam)
                 .compact();
     }

     public Claims getClaimsFromToken(String token) {
         return Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                 .parseClaimsJws(token)
                 .getBody();
     }

     public String getUsernameFromToken(String token) {
         return getClaimsFromToken(token).getSubject();
     }

     public Date getExpirationDate(String token) {
         return getClaimsFromToken(token).getExpiration();
     }

     public Boolean isTokenExpired(String token) {
         return getExpirationDate(token).before(new Date());
     }

     public Boolean isTokenValidated(String token) {
         return !isTokenExpired(token);
     }
}
