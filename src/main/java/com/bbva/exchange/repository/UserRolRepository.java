package com.bbva.exchange.repository;

import com.bbva.exchange.model.UserRol;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UserRolRepository extends ReactiveCrudRepository<UserRol, UUID> {
    Flux<UserRol> findByUserId(UUID userId);
}
