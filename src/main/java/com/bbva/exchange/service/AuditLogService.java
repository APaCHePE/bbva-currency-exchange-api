package com.bbva.exchange.service;

import com.bbva.exchange.exception.BusinessException;
import com.bbva.exchange.model.AuditLog;
import com.bbva.exchange.repository.AuditLogRepository;
import com.bbva.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

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

    /**
     * Registra una acción de auditoría con detalles personalizados.
     *
     * @param username Usuario que realizó la acción
     * @param action   Tipo de acción (ej: CREATE_EXCHANGE_RATE)
     * @param details  Detalles relevantes
     * @return Mono<Void>
     */
    public Mono<Void> registerAudit(UUID userId, String username, String action, String details) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAction(action);
        log.setDetails(details);
        log.setTimestamp(LocalDateTime.now());
        System.out.println("Audit log: " + log);
        return auditLogRepository.save(log).then();
    }
}