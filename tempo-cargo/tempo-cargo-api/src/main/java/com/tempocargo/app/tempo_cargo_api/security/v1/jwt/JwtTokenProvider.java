package com.tempocargo.app.tempo_cargo_api.security.v1.jwt;

import com.tempocargo.app.tempo_cargo_api.common.v1.exception.InvalidVerificationTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
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

    @Value("${jwt.refreshTokenValiditySec:1209600}")
    private long refreshTokenValiditySec;

    @Value("${jwt.emailVerificationTokenValiditySec:300}")
    private long emailVerificationTokenValiditySec;

    @Value("${jwt.emailVerifiedTokenValiditySec:600}")
    private long emailVerifiedTokenValiditySec;

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
                .claim("type", "ACCESS")
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
                .claim("type", "REFRESH")
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailVerificationToken(String email, String otp) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(emailVerificationTokenValiditySec);
        return Jwts.builder()
                .setSubject(email)
                .claim("otp", otp)
                .claim("type", "EMAIL_VERIFICATION")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateEmailVerifiedToken(String email) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(emailVerifiedTokenValiditySec);

        return Jwts.builder()
                .setSubject(email)
                .claim("verified", true)
                .claim("type", "EMAIL_VERIFIED")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateEmailVerifiedToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidVerificationTokenException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7).trim();

        try {
            Jws<Claims> jws = parseToken(token);
            Claims claims = jws.getBody();

            String type = claims.get("type", String.class);
            Boolean verified = claims.get("verified", Boolean.class);

            if (!"EMAIL_VERIFIED".equals(type) || !Boolean.TRUE.equals(verified)) {
                throw new InvalidVerificationTokenException("Invalid or unverified token type");
            }

            return claims.getSubject();
        } catch (JwtException ex) {
            throw new InvalidVerificationTokenException("Invalid or expired verification token");
        }
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

    public Instant getEmailVerificationTokenExpiryInstant() {
        return Instant.now().plusSeconds(emailVerificationTokenValiditySec);
    }
}
