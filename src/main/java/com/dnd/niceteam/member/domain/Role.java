package com.dnd.niceteam.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER","ROLE_USER"),ADMIN("ADMIN","ROLE_ADMIN");

    private final String name;
    private final String fullName;

    Role ( String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }
}
