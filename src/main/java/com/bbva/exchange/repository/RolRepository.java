package com.bbva.exchange.repository;

import com.bbva.exchange.model.Roles;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RolRepository extends ReactiveCrudRepository<Roles, UUID> {
    Mono<Roles> findById(UUID id);
}
