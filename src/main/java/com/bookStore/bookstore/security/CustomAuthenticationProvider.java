package com.bookStore.bookstore.security;

import com.bookStore.bookstore.module.client.exception.ClientNotFoundException;
import com.bookStore.bookstore.module.client.model.Client;
import com.bookStore.bookstore.module.client.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final ClientService clientService;
    private final PasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();

        Client userFound = clientService.getClientForAuthentication(login)
                .orElseThrow(() -> new ClientNotFoundException("Incorrect username or password"));

        String encryptedPassword = userFound.getPassword();

        boolean passwordEquals = encoder.matches(password, encryptedPassword);

        if(passwordEquals){
            return new CustomAuthentication(userFound);
        }

        throw new ClientNotFoundException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }
}
