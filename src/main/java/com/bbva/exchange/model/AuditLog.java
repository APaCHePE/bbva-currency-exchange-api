package com.bbva.exchange.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("audit_logs")
@Schema(description = "Detalle de un registro de auditoría del sistema")
public class AuditLog {
    @Id
    @Schema(description = "Identificador único del registro", example = "f399a5a5-7107-451f-bfc5-b247e0df7606")
    private UUID id;

    @Schema(description = "Identificador del usuario que realizó la acción", example = "85842ec0-2829-4b09-8367-0c467b1efd88")
    private UUID userId;

    @Schema(description = "Nombre de usuario", example = "admin")
    private String username;

    @Schema(description = "Acción realizada por el usuario", example = "CONSULT_EXCHANGE_RATE")
    private String action;

    @Schema(description = "Fecha y hora en que se realizó la acción", example = "2025-05-16T09:00:36.521096")
    private LocalDateTime timestamp;

    @Schema(description = "Detalles adicionales sobre la acción realizada", example = "Consulted exchange rate from USD to PEN with rate 3.9800")
    private String details;
}
