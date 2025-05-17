package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Respuesta exitosa del login con el token JWT")
public class AuthResponse {
    @Schema(description = "Token JWT generado", example = "eyJhbGciOiJIUzI1NiIsIn...")
    private final String token;
}