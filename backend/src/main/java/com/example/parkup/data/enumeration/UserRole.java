package com.example.parkup.data.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER,
    ROLE_REG_USER,
    ROLE_WORKER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}

