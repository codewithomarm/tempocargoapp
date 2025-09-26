package com.tempocargo.app.tempo_cargo_api.security.v1.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenValiditySec:900}")
    private long accessTokenValiditySec;

    @Value("${jwt.refreshTokenValiditySec:1209600}") // 14 days default
    private long refreshTokenValiditySec;

    private Key signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes;
        try {
            // Intentar decodificar Base64 (si JWT_SECRET es un Base64)
            keyBytes = Base64.getDecoder().decode(jwtSecret);
            // si la cadena no es Base64 válida esto lanzará IllegalArgumentException y caemos al catch
        } catch (IllegalArgumentException ex) {
            // No estaba en Base64: usar bytes del string directamente (asegúrate de que tenga al menos 32 chars)
            keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        }
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String subject, List<String> roles) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(accessTokenValiditySec);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("roles", roles)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String subject) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(refreshTokenValiditySec);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Jws<Claims> parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }

    public Instant getAccessTokenExpiryInstant() {
        return Instant.now().plusSeconds(accessTokenValiditySec);
    }

    public Instant getRefreshTokenExpiryInstant() {
        return Instant.now().plusSeconds(refreshTokenValiditySec);
    }
}
