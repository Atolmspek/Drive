package com.martin.Drive.service;

import com.martin.Drive.dao.UserRepository;
import com.martin.Drive.entity.User;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JwtService {

    @Autowired
    private UserRepository userRepository;

    private static final String secretKey = "ajskdfashgvhcaehdbwq3367215assjkkhbgyiuuuuttttttttttyyyyyyyyyyydtrdtrkhjzx";

    public String generateToken(String userName) {
        Map<String, Object> claims = new HashMap<>();
        User user = userRepository.findByusername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + userName));
        // Esto vendría siendo el email en uso real claims.put("username", userName);
       //Para Tetris:
        claims.put("nombre", user.getNombre());
        //Para Calendario
        claims.put("roles", user.getRoles());

        //Para Update
        claims.put("id", user.getId());
        // 30 días de caducidad
        Date expirationDate = new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName= extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
