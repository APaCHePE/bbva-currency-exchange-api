package com.bbva.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import com.bbva.exchange.dto.AuthRequest;
import com.bbva.exchange.dto.AuthResponse;
import com.bbva.exchange.dto.GenericResponse;
import com.bbva.exchange.service.AuthService;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con el acceso al sistema")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
        this.authService = authService;
    }

  @Operation(
    summary = "Iniciar sesión",
    description = "Permite autenticar a un usuario utilizando sus credenciales y obtener un token JWT.",
    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "Credenciales del usuario (username y password)",
      required = true,
      content = @Content(
        schema = @Schema(implementation = AuthRequest.class),
        examples = @ExampleObject(
          name = "Login Example",
          value = """
                        {
                            "username": "admin",
                            "password": "admin123"
                        }
                        """
        )
      )
    ),
    responses = {
      @ApiResponse(
        responseCode = "200",
        description = "Autenticación exitosa",
        content = @Content(
          schema = @Schema(implementation = GenericResponse.class),
          examples = @ExampleObject(
            name = "Login Success Response",
            value = """
                            {
                                "success": true,
                                "message": "Authentication successful",
                                "data": {
                                    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                }
                            }
                            """
          )
        )
      ),
      @ApiResponse(
        responseCode = "400",
        description = "Datos incompletos o credenciales inválidas",
        content = @Content(
          schema = @Schema(implementation = GenericResponse.class),
          examples = {
            @ExampleObject(
              name = "Credenciales inválidas",
              value = """
                                {
                                    "success": false,
                                    "message": "Invalid credentials",
                                    "data": null
                                }
                                """
            ),
            @ExampleObject(
              name = "Faltan datos",
              value = """
                                {
                                    "success": false,
                                    "message": "rawPassword cannot be null",
                                    "data": null
                                }
                                """
            )
          }
        )
      ),
      @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor",
        content = @Content(schema = @Schema(implementation = GenericResponse.class))
      )
    }
  )
  @PostMapping("/login")
  public Mono<ResponseEntity<GenericResponse<AuthResponse>>> login(@RequestBody AuthRequest request) {
    return authService.authenticate(request)
              .map(authResponse -> ResponseEntity.ok(GenericResponse.success("Login successful", authResponse)));
  }
}