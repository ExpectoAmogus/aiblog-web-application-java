package com.expectoamogus.aiblog.models.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public enum Role{
    ROLE_USER(Set.of(Permission.USERS_READ)),
    ROLE_ADMIN(Set.of(Permission.DEVS_READ, Permission.DEVS_WRITE, Permission.USERS_READ)),
    ROLE_MODERATOR(Set.of(Permission.DEVS_READ, Permission.MODS_WRITE, Permission.USERS_READ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
