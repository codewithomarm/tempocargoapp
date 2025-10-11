package com.tempocargo.app.tempo_cargo_api.security.v1.jwt;

import com.tempocargo.app.tempo_cargo_api.auth.v1.session.model.Session;
import com.tempocargo.app.tempo_cargo_api.auth.v1.session.repository.SessionRepository;
import com.tempocargo.app.tempo_cargo_api.security.v1.util.TokenHashUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final TokenHashUtil tokenHashUtil;
    private final SessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Jws<Claims> parsed = jwtTokenProvider.parseToken(token);
            Claims claims = parsed.getBody();

            String type = claims.get("type", String.class);
            if (type != null && !"ACCESS".equals(type)) {
                filterChain.doFilter(request, response);
                return;
            }

            String username = claims.getSubject();
            if (username == null || username.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            String accessHash = tokenHashUtil.sha256Hex(token);
            Session session = sessionRepository.findByAccessTokenHash(accessHash).orElse(null);
            if (session == null || !Boolean.TRUE.equals(session.getIsActive())) {
                log.debug("No active session found for access token hash {}", accessHash);
                filterChain.doFilter(request, response);
                return;
            }

            LocalDateTime expiresAt = session.getExpiresAt();
            if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) {
                log.debug("Session expired at {} for user {}", expiresAt, username);
                filterChain.doFilter(request, response);
                return;
            }

            // 4) if SecurityContext is empty, load user and set authentication
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailsService.loadUserByUsername(username);

                // build authorities (we rely on UserDetails authorities)
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 5) update last activity of session (optional)
                try {
                    LocalDateTime now = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
                    sessionRepository.updateLastActivityByAccessTokenHash(accessHash, now);
                } catch (Exception e) {
                    log.debug("Failed to update lastActivity for session: {}", e.getMessage());
                }
            }

        } catch (JwtException ex) {
            // invalid token (signature, malformed, expired)
            log.debug("Invalid JWT token: {}", ex.getMessage());
            // do not set authentication, just continue (request will be denied later if auth required)
        } catch (Exception ex) {
            // unexpected errors: log and continue filter chain
            log.error("Error processing JWT authentication filter", ex);
        }

        filterChain.doFilter(request, response);
    }

}
