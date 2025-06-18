package com.bookStore.bookstore.security;

import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.client.service.ClientService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final ClientService service;

    public CustomUserDetailsService(ClientService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = service.getClientByUsername(username);

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(client.getUsername())
                .password(client.getPassword())
                .roles(client.getRoles().toArray(new String[0]))
                .build();
    }
}


