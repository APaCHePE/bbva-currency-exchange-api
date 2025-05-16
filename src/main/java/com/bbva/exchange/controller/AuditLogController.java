package com.bbva.exchange.controller;

import com.bbva.exchange.dto.GenericResponse;
import com.bbva.exchange.model.AuditLog;
import com.bbva.exchange.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public Flux<ResponseEntity<GenericResponse<AuditLog>>> getAuditLogs() {
        return auditLogService.getAuditLogs()
                .map(log -> ResponseEntity.ok(GenericResponse.success("Audit log retrieved successfully", log)))
                .onErrorResume(e -> Flux.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}