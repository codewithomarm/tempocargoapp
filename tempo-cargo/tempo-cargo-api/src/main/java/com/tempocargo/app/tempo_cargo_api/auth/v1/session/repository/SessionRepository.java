package com.tempocargo.app.tempo_cargo_api.auth.v1.session.repository;

import com.tempocargo.app.tempo_cargo_api.auth.v1.session.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // --- Búsquedas por token (hash) --------------------
    Optional<Session> findByAccessTokenHash(String accessTokenHash);
    Optional<Session> findByRefreshTokenHash(String refreshTokenHash);

    // --- Comprobaciones rápidas ------------------------
    boolean existsByAccessTokenHash(String accessTokenHash);
    boolean existsByRefreshTokenHash(String refreshTokenHash);

    // --- Sesiones activas por usuario ------------------
    List<Session> findAllByUserIdAndIsActiveTrue(Long userId);
    long countByUserIdAndIsActiveTrue(Long userId);

    // --- Revocar / desactivar sesión por token (modificar) ---
    @Modifying
    @Query("update Session s set s.isActive = false, s.lastActivityAt = current_timestamp where s.accessTokenHash = :accessTokenHash")
    int markRevokedByAccessTokenHash(@Param("accessTokenHash") String accessTokenHash);

    @Modifying
    @Query("update Session s set s.isActive = false, s.lastActivityAt = current_timestamp where s.refreshTokenHash = :refreshTokenHash")
    int markRevokedByRefreshTokenHash(@Param("refreshTokenHash") String refreshTokenHash);

    // --- Actualizar lastActivity (p. ej. al usar refresh) ---
    @Modifying
    @Query("update Session s set s.lastActivityAt = :lastActivityAt where s.accessTokenHash = :accessTokenHash")
    int updateLastActivityByAccessTokenHash(@Param("accessTokenHash") String accessTokenHash,
                                            @Param("lastActivityAt") LocalDateTime lastActivityAt);

    // --- Limpieza / búsqueda de expirados -----------------
    List<Session> findAllByExpiresAtBeforeAndIsActiveTrue(LocalDateTime timeNow);
}