package com.bookStore.bookstore.module.client.controller;

import com.bookStore.bookstore.module.client.DTO.AuthResponseDTO;
import com.bookStore.bookstore.module.client.DTO.LoginRequestDTO;
import com.bookStore.bookstore.module.client.model.StatusClient;
import com.bookStore.bookstore.module.client.repository.ClientRepository;
import com.bookStore.bookstore.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class ClientAuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final ClientRepository repository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);

        var user = repository.findByUsernameAndStatus(username, StatusClient.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        AuthResponseDTO authDTO = new AuthResponseDTO(
                token,
                username,
                user.getEmail(),
                user.getRoles());
        return ResponseEntity.ok(authDTO);
    }
}



