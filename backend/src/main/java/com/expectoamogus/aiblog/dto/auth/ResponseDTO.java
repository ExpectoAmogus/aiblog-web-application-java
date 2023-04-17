package com.expectoamogus.aiblog.dto.auth;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record ResponseDTO(String token, Collection<? extends GrantedAuthority> authorities) {
}
