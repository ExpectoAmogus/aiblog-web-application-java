package com.expectoamogus.aiblog.models.enums;

import lombok.Getter;

@Getter
public enum Permission {
    USERS_READ("users:read"),
    USERS_WRITE("users:write"),
    DEVS_READ("devs:read"),
    DEVS_WRITE("devs:write"),
    MODS_READ("mods:read"),
    MODS_WRITE("mods:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

}
