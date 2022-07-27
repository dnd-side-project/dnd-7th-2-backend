package com.dnd.niceteam.domain.account;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum Role implements GrantedAuthority {
    USER("USER","ROLE_USER"),ADMIN("ADMIN","ROLE_ADMIN");

    private final String name;
    private final String fullName;

    Role ( String name, String fullName) {
        this.name = name;
        this.fullName = fullName;
    }

    @Override
    public String getAuthority() {
        return this.fullName;
    }

}
