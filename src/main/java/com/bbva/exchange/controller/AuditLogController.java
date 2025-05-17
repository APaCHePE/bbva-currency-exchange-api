package com.bbva.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import com.bbva.exchange.dto.GenericResponse;
import com.bbva.exchange.model.AuditLog;
import com.bbva.exchange.service.AuditLogService;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
@Tag(name = "Registro de Auditoría", description = "Operaciones para consultar los registros de auditoría del sistema")
public class AuditLogController {

    private final AuditLogService auditLogService;
    @Operation(
      summary = "Obtener todos los registros de auditoría",
      description = "Recupera el historial de acciones realizadas por los usuarios en el sistema.",
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Registros de auditoría obtenidos exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Audit Log Success Example",
              value = """
                        {
                            "success": true,
                            "message": "Audit log retrieved successfully",
                            "data": {
                                "id": "f399a5a5-7107-451f-bfc5-b247e0df7606",
                                "userId": "85842ec0-2829-4b09-8367-0c467b1efd88",
                                "action": "CONSULT_EXCHANGE_RATE",
                                "details": "Consulted exchange rate from USD to PEN with rate 3.9800",
                                "timestamp": "2025-05-16T09:00:36.521096",
                                "username": "admin"
                            }
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error al obtener los registros de auditoría",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Audit Log Error Example",
              value = """
                        {
                            "success": false,
                            "message": "Failed to retrieve audit logs",
                            "data": null
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @GetMapping
    public Flux<ResponseEntity<GenericResponse<AuditLog>>> getAuditLogs() {
        return auditLogService.getAuditLogs()
                .map(log -> ResponseEntity.ok(GenericResponse.success("Audit log retrieved successfully", log)))
                .onErrorResume(e -> Flux.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}