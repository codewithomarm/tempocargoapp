package com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.repository;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.model.TempoRoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TempoRoleUserRepository extends JpaRepository<TempoRoleUser, Long> {

    @Query("select tru from TempoRoleUser tru where tru.user.id = :userId")
    List<TempoRoleUser> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("delete from TempoRoleUser tru " +
            "where tru.user.id = :userId and tru.role.id = :roleId")
    void deleteByUserIdAndRoleId(@Param("userId") Long userId,
                                 @Param("roleId") Long roleId);

    @Query("select case when count(tru) > 0 then true else false end " +
            "from TempoRoleUser tru " +
            "where tru.user.id = :userId and tru.role.id = :roleId")
    boolean existsByUserIdAndRoleId(@Param("userId") Long userId,
                                    @Param("roleId") Long roleId);

    @Modifying
    @Transactional
    @Query("delete from TempoRoleUser tru where tru.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);

    @Query("SELECT tru.role FROM TempoRoleUser tru WHERE tru.user.id = :userId")
    List<TempoRole> findRolesByUserId(@Param("userId") Long userId);

    @Query("SELECT r.name FROM TempoRoleUser tru JOIN tru.role r WHERE tru.user.id = :userId")
    List<String> findRoleNamesByUserId(@Param("userId") Long userId);

    @Query("SELECT tru.user.id FROM TempoRoleUser tru JOIN tru.role r WHERE r.name = :roleName")
    List<Long> findUserIdsByRoleName(@Param("roleName") String roleName);
}
