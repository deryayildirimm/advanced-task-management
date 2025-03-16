package com.example.advancedtaskmanagement.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_MOD("MOD"),
    ROLE_FSK("FSK"),
    ROLE_PROJECT_GROUP_MANAGER("PROJECT_GROUP_MANAGER"),
    ROLE_PROJECT_MANAGER("PROJECT_MANAGER"),
    ROLE_TEAM_MEMBER("TEAM_MEMBER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String getAuthority() {
        return name();
    }
}
