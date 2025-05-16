package com.bbva.exchange.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("audit_log")
public class AuditLog {
    @Id
    private UUID id;
    private String username;
    private String action;
    private LocalDateTime timestamp;
    private String details;
}