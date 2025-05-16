package com.bbva.exchange.service;

import com.bbva.exchange.dto.*;
import com.bbva.exchange.exception.BusinessException;
import com.bbva.exchange.model.ExchangeRate;
import com.bbva.exchange.repository.ExchangeRateRepository;
import com.bbva.exchange.util.AppConstants;
import com.bbva.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;

    public Flux<ExchangeRateResponse> getAllExchangeRates() {
        return exchangeRateRepository.findAll()
                .switchIfEmpty(Flux.error(new BusinessException("No exchange rates found")))
                .map(rate -> new ExchangeRateResponse(
                        rate.getId(),
                        rate.getFromCurrency(),
                        rate.getToCurrency(),
                        rate.getRate(),
                        rate.getUpdatedAt()
                ))
                .onErrorMap(e -> new BusinessException("Error retrieving exchange rates: " + e.getMessage()));
    }

    public Mono<ExchangeRateResponse> createExchangeRate(ExchangeRateRequest request) {
        return SecurityUtil.requireAdmin()
                .then(Mono.defer(() -> {
                    ExchangeRate rate = new ExchangeRate();
                    rate.setId(UUID.randomUUID());
                    rate.setFromCurrency(request.getFromCurrency());
                    rate.setToCurrency(request.getToCurrency());
                    rate.setRate(request.getRate());
                    rate.setUpdatedAt(LocalDateTime.now());

                    return exchangeRateRepository.save(rate)
                            .map(saved -> new ExchangeRateResponse(
                                    saved.getId(),
                                    saved.getFromCurrency(),
                                    saved.getToCurrency(),
                                    saved.getRate(),
                                    saved.getUpdatedAt()
                            ));
                }));
    }

    public Mono<ExchangeRateResponse> updateExchangeRate(UUID id, UpdateExchangeRateRequest request) {
        return SecurityUtil.requireAdmin()
                .then(exchangeRateRepository.findById(id)
                        .switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found")))
                        .flatMap(existing -> {
                            existing.setRate(request.getRate());
                            existing.setUpdatedAt(LocalDateTime.now());
                            return exchangeRateRepository.save(existing);
                        })
                        .map(updated -> new ExchangeRateResponse(
                                updated.getId(),
                                updated.getFromCurrency(),
                                updated.getToCurrency(),
                                updated.getRate(),
                                updated.getUpdatedAt()
                        ))
                );
    }

    public Mono<ExchangeRateResponse> getExchangeRateByCurrencies(String from, String to) {
        return exchangeRateRepository.findAll()
                .filter(rate -> rate.getFromCurrency().equalsIgnoreCase(from) && rate.getToCurrency().equalsIgnoreCase(to))
                .next()
                .switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found for " + from + " to " + to)))
                .map(rate -> new ExchangeRateResponse(
                        rate.getId(),
                        rate.getFromCurrency(),
                        rate.getToCurrency(),
                        rate.getRate(),
                        rate.getUpdatedAt()
                ));
    }
    public Mono<ConversionResponse> convertCurrency(ConversionRequest request) {
        return exchangeRateRepository.findAll()
                .filter(rate -> rate.getFromCurrency().equalsIgnoreCase(request.getFrom()) &&
                        rate.getToCurrency().equalsIgnoreCase(request.getTo()))
                .next()
                .switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found for " + request.getFrom() + " to " + request.getTo())))
                .map(rate -> {
                    BigDecimal convertedAmount = request.getAmount().multiply(rate.getRate());
                    return new ConversionResponse(request.getAmount(), convertedAmount, rate.getRate());
                });
    }
}