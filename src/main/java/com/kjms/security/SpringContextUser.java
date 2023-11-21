package com.kjms.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static com.kjms.security.jwt.TokenProvider.*;

public class SpringContextUser implements UserDetails {

    private final String id;

    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean enabled;

    public SpringContextUser(
        String id,
        String password,
        List<GrantedAuthority> authorities,
        boolean activated
    ) {
        this.id = id;
        this.password = password;
        this.authorities = authorities;
        this.enabled = activated;
        this.accountNonExpired = true;
        this.accountNonLocked = true;

    }

    public SpringContextUser(Claims claims) {
        this.id = claims.getSubject();

        this.password = "";
        this.authorities =
            Arrays
                .stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        this.enabled = true;
        this.accountNonExpired = true;
        this.accountNonLocked = true;
    }



    public String getId() {
        return id;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
