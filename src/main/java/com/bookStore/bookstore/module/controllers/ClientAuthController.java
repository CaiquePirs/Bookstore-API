package com.bookStore.bookstore.module.controllers;

import com.bookStore.bookstore.docs.ClientAuthApi;
import com.bookStore.bookstore.module.common.login.LoginResponseDTO;
import com.bookStore.bookstore.module.common.login.LoginRequestDTO;
import com.bookStore.bookstore.module.enums.StatusEntity;
import com.bookStore.bookstore.module.repositories.ClientRepository;
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
public class ClientAuthController implements ClientAuthApi {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final ClientRepository repository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginDTO) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password()));

        String username = authentication.getName();
        String token = jwtUtil.generateToken(username);

        var user = repository.findByUsernameAndStatus(username, StatusEntity.ACTIVE)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        LoginResponseDTO authDTO = new LoginResponseDTO(
                token,
                username,
                user.getEmail(),
                user.getRoles());
        return ResponseEntity.ok(authDTO);
    }
}



