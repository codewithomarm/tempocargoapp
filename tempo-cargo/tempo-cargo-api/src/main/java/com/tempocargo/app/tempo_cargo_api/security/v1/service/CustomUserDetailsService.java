package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import com.tempocargo.app.tempo_cargo_api.auth.v1.roleuser.repository.TempoRoleUserRepository;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository.TempoUserRepository;
import com.tempocargo.app.tempo_cargo_api.security.v1.adapter.CustomUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TempoUserRepository tempoUserRepository;
    private final TempoRoleUserRepository tempoRoleUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Find TempoUser by username in repo
        TempoUser user = tempoUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Find Roles by TempoUser.id in repo
        List<String> roleNames = tempoRoleUserRepository.findRoleNamesByUserId(user.getId());

        // Map Repo Roles to GrantedAuthorities (ROLE_NAME)
        List<SimpleGrantedAuthority> authorities = roleNames
                .stream()
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                .toList();

        // Use CustomUserDetails (implements UserDetails)
        return CustomUserDetails.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .enabled(user.getEnabled())
                .accountNonLocked(user.getAccountNonLocked())
                .build();
    }
}
