package com.bbva.exchange.repository;

import com.bbva.exchange.model.ExchangeRate;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ExchangeRateRepository extends ReactiveCrudRepository<ExchangeRate, UUID> {

    /**
     * Busca todos los tipos de cambio registrados.
     */
    @Override
    Flux<ExchangeRate> findAll();

    /**
     * Busca un tipo de cambio específico por su ID.
     */
    @Override
    Mono<ExchangeRate> findById(UUID id);

    /**
     * Busca un tipo de cambio por moneda origen y destino.
     */
    Mono<ExchangeRate> findByFromCurrencyAndToCurrency(String fromCurrency, String toCurrency);

    /**
     * Guarda o actualiza un tipo de cambio.
     */
    @Override
    <S extends ExchangeRate> Mono<S> save(S entity);

    /**
     * Elimina un tipo de cambio por su ID.
     */
    @Override
    Mono<Void> deleteById(UUID id);
}