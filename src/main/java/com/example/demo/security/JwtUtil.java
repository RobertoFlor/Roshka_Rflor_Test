package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
  private final Key key;
  private final long expMillis;

  public JwtUtil(@Value("${app.jwt.secret}") String secret,
                 @Value("${app.jwt.exp-minutes}") long expMin) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expMillis = expMin * 60_000;
  }

  public String generate(String username){
    Instant now = Instant.now();
    return Jwts.builder()
      .setSubject(username)
      .addClaims(Map.of("typ","access"))
      .setIssuedAt(Date.from(now))
      .setExpiration(new Date(now.toEpochMilli() + expMillis))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public String validateAndGetSubject(String token){
    return Jwts.parserBuilder().setSigningKey(key).build()
      .parseClaimsJws(token).getBody().getSubject();
  }
}
