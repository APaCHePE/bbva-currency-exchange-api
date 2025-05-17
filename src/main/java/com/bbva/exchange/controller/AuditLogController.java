package com.bbva.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
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
            @ApiResponse(responseCode = "200", description = "Registros de auditoría obtenidos exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error al obtener los registros de auditoría"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
        }
    )
    @GetMapping
    public Flux<ResponseEntity<GenericResponse<AuditLog>>> getAuditLogs() {
        return auditLogService.getAuditLogs()
                .map(log -> ResponseEntity.ok(GenericResponse.success("Audit log retrieved successfully", log)))
                .onErrorResume(e -> Flux.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}