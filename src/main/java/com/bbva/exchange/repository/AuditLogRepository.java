package com.bbva.exchange.repository;

import com.bbva.exchange.model.AuditLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface AuditLogRepository extends ReactiveCrudRepository<AuditLog, UUID> {
}
