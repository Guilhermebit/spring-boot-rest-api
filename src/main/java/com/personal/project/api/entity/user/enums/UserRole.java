package com.personal.project.api.entity.user.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    ADMIN("admin"),
    USER("user");

    private final String role;

    UserRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return role;
    }

}

