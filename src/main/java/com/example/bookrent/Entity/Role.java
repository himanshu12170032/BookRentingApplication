package com.example.bookrent.Entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

public enum Role {
    ADMIN,
    STUDENTS,
    PROFESSIONAL,
    CASUAL_READER,
    PREMIUM_USER;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities= new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(getRoleName()));
        return authorities;
    }
}
