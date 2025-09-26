package com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.model;

import com.tempocargo.app.tempo_cargo_api.auth.v1.role.model.TempoRole;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(schema = "auth", name = "tempo_role_user",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"role_id", "user_id"},
                    name = "role_user_roleId_userId_UNIQUE")
        },
        indexes = {
            @Index(columnList = "role_id", name = "fk_role_user_role_idx"),
            @Index(columnList = "user_id", name = "fk_role_user_user_idx")
        })
@Builder
public class TempoRoleUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "TempoRoleUser's role should not be null")
    @JoinColumn(name = "role_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_role_user_role"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TempoRole role;

    @ManyToOne
    @NotNull(message = "TempoRoleUser's user should not be null")
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_role_user_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TempoUser user;

    @EqualsAndHashCode.Include
    public Long getRoleId() { return role != null ? role.getId() : null; }

    @EqualsAndHashCode.Include
    public Long getUserId() { return user != null ? user.getId() : null; }
}
