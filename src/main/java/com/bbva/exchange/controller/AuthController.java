package com.bbva.exchange.controller;

import com.bbva.exchange.dto.AuthRequest;
import com.bbva.exchange.dto.AuthResponse;
import com.bbva.exchange.dto.GenericResponse;
import com.bbva.exchange.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<GenericResponse<AuthResponse>>> login(@RequestBody AuthRequest request) {
        return authService.authenticate(request)
                .map(authResponse -> ResponseEntity.ok(GenericResponse.success("Login successful", authResponse)));
    }
}