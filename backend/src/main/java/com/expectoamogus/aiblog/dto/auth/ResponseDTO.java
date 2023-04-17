package com.expectoamogus.aiblog.dto.auth;

import com.expectoamogus.aiblog.models.enums.Role;

public record ResponseDTO(String token, Role role) {
}
