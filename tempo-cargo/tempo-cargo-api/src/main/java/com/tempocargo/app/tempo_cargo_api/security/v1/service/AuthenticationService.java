package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.model.RevokedToken;
import com.tempocargo.app.tempo_cargo_api.auth.v1.revokedtoken.repository.RevokedTokenRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.repository.TempoRoleUserRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.session.model.Session;
import com.tempocargo.app.tempo_cargo_api.auth.v1.session.repository.SessionRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository.TempoUserRepository;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.LoginRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.AuthResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.jwt.JwtTokenProvider;
import com.tempocargo.app.tempo_cargo_api.security.v1.util.TokenHashUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenHashUtil tokenHashUtil;
    private final SessionRepository sessionRepository;
    private final TempoUserRepository tempoUserRepository;
    private final TempoRoleUserRepository tempoRoleUserRepository;
    private final RevokedTokenRepository revokedTokenRepository;

    public AuthResponse login(LoginRequest request, HttpServletRequest httpServletRequest){
        // Authenticate username/password with AuthManager
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // authentication.getPrincipal() is a UserDetails
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // generate tokens
        String accessToken = jwtTokenProvider.generateAccessToken
                (userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toList()
                );
        String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

        // hash tokens
        String accessHash = tokenHashUtil.sha256Hex(accessToken);
        String refreshHash = tokenHashUtil.sha256Hex(refreshToken);

        // find TempoUser entity
        TempoUser tempoUser = tempoUserRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found after auth: " + userDetails.getUsername()));

        // persist session with refresh expiry as session.expiresAt
        Instant refreshExpiry = jwtTokenProvider.getRefreshTokenExpiryInstant();
        Session session = new Session();
        session.setUser(tempoUser);
        session.setAccessTokenHash(accessHash);
        session.setRefreshTokenHash(refreshHash);
        session.setExpiresAt(LocalDateTime.ofInstant(refreshExpiry, ZoneId.systemDefault()));
        session.setIpAddress(extractIp(httpServletRequest));
        session.setUserAgent(httpServletRequest != null ? httpServletRequest.getHeader("User-Agent") : null);
        session.setIsActive(true);
        sessionRepository.save(session);

        return new AuthResponse(
                accessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiryInstant(),
                refreshExpiry
        );
    }

    public AuthResponse refresh(String refreshToken, HttpServletRequest httpServletRequest) {
        String refreshHash = tokenHashUtil.sha256Hex(refreshToken);

        Session session = sessionRepository.findByRefreshTokenHash(refreshHash)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (!Boolean.TRUE.equals(session.getIsActive())) {
            throw new BadCredentialsException("Session is not active");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Refresh token expired");
        }

        String username = session.getUser().getUsername();

        List<String> userRoles = tempoRoleUserRepository.findRoleNamesByUserId(session.getUser().getId());
        String newAccessToken = jwtTokenProvider.generateAccessToken(username,userRoles);

        String newAccessHash = tokenHashUtil.sha256Hex(newAccessToken);

        session.setAccessTokenHash(newAccessHash);
        session.setLastActivityAt(LocalDateTime.now());
        sessionRepository.save(session);

        return new AuthResponse(
                newAccessToken,
                refreshToken,
                jwtTokenProvider.getAccessTokenExpiryInstant(),
                session.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()
        );
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String accessToken = authHeader.substring(7);

        String accessTokenHash = tokenHashUtil.sha256Hex(accessToken);

        Session session = sessionRepository.findByAccessTokenHash(accessTokenHash)
                .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

        if (!Boolean.TRUE.equals(session.getIsActive())) {
            throw new BadCredentialsException("Session is not active");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Refresh token already expired");
        }

        session.setIsActive(false);
        sessionRepository.save(session);

        String refreshTokenHash = session.getRefreshTokenHash();

        RevokedToken revokedToken = new RevokedToken();
        revokedToken.setTokenHash(refreshTokenHash);
        revokedTokenRepository.save(revokedToken);
    }

    private String extractIp(HttpServletRequest request) {
        if (request == null) return null;
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
