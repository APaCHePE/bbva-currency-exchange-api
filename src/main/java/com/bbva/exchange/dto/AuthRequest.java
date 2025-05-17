package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Schema(description = "Credenciales de acceso del usuario")
public class AuthRequest {
    @Schema(description = "Nombre de usuario", example = "user")
    private String username;

    @Schema(description = "Contraseña del usuario", example = "user123")
    private String password;

}
