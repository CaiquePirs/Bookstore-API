package com.bookStore.bookstore.security;

import com.bookStore.bookstore.module.client.model.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Client client;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.client.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public Object getCredentials() {
        return client;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return client;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return client.getUsername();
    }
}
