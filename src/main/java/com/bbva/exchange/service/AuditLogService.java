package com.bbva.exchange.service;

import com.bbva.exchange.exception.BusinessException;
import com.bbva.exchange.model.AuditLog;
import com.bbva.exchange.repository.AuditLogRepository;
import com.bbva.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public Flux<AuditLog> getAuditLogs() {
        // Verifica que el usuario sea ADMIN antes de obtener los registros
        return SecurityUtil.requireAdmin()
                .thenMany(auditLogRepository.findAll())
                .switchIfEmpty(Flux.error(new BusinessException("No audit logs found")));
    }
}