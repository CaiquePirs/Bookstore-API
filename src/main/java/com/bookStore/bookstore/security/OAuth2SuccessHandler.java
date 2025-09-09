package com.bookStore.bookstore.security;

import com.bookStore.bookstore.module.dtos.ClientRequestDTO;
import com.bookStore.bookstore.module.entities.Client;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.mappers.ClientMapper;
import com.bookStore.bookstore.module.repositories.ClientRepository;
import com.bookStore.bookstore.module.utils.UtilsMethods;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final String PASSWORD_DEFAULT = System.getenv("PASSWORD_DEFAULT");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if(clientRepository.findByEmailAndStatus(email, StatusEntity.ACTIVE).isEmpty()){
            String usernameFromEmail = UtilsMethods.extractNameFromEmail(email);

            ClientRequestDTO dto = new ClientRequestDTO(
                    usernameFromEmail,
                    email,
                    PASSWORD_DEFAULT,
                    LocalDate.now()
            );

            Client client = clientMapper.toEntity(dto);
            client.setRoles(List.of("USER"));
            client.setStatus(StatusEntity.ACTIVE);
            clientRepository.save(client);
        }

        String token = jwtUtil.generateToken(email);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }
}

