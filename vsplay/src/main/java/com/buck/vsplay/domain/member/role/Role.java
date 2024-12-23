package com.buck.vsplay.domain.member.role;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Role {

    GENERAL(1, "ROLE_GENERAL");

    private final int level;
    private final String roleName;

    Role(int level, String roleName){
        this.level = level;
        this.roleName = roleName;
    }

    public boolean isHigherThan(Role otherRole){
        return this.level < otherRole.level;
    }

    public static Role fromRoleName(String roleName){
        return Arrays.stream(Role.values())
                .filter( role -> role.getRoleName().equals(roleName))
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException("Invalid role name: " + roleName));
    }
}

