package com.bookStore.bookstore.security;

import com.bookStore.bookstore.module.client.DTO.ClientRequestDTO;
import com.bookStore.bookstore.module.client.mappers.ClientMapper;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.repository.ClientRepository;
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

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final String PASSWORD_DEFAULT = System.getenv("PASSWORD_DEFAULT");

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        var existClient = clientRepository.findByEmailAndStatus(email, StatusClient.ACTIVE);
        if(existClient.isEmpty()){
            var clientFromEmail = extractNameFromEmail(email);

            ClientRequestDTO dto = new ClientRequestDTO(
                    clientFromEmail,
                    email,
                    PASSWORD_DEFAULT,
                    LocalDate.now()
            );

            var clientEntity = clientMapper.toEntity(dto);
            clientEntity.setRoles(List.of("USER"));
            clientEntity.setStatus(StatusClient.ACTIVE);
            clientRepository.save(clientEntity);
        }

        String token = jwtUtil.generateToken(email);

        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
    }

    public static String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "";
        }
        int atIndex = email.indexOf('@');

        if (atIndex > 0) {
            return email.substring(0, atIndex);
        } else {
            return "";
        }
    }

}

