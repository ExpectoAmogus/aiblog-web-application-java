package com.expectoamogus.aiblog.dto.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public record UserDTO(
        Long id,
        String firstName,
        String email,
        Set<SimpleGrantedAuthority> role,
        String username,
        List<Long> articleIds,
        String dateOfCreated
) {
}
