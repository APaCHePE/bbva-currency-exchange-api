package com.bbva.exchange.controller;

import com.bbva.exchange.dto.*;
import com.bbva.exchange.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping
    public Mono<ResponseEntity<GenericResponse<List<ExchangeRateResponse>>>> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates()
                .collectList()
                .map(rates -> ResponseEntity.ok(GenericResponse.success("Exchange rates retrieved successfully", rates)));
    }

    @PostMapping
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> createExchangeRate(
            @RequestBody ExchangeRateRequest request) {

        return exchangeRateService.createExchangeRate(request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate registered successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> updateExchangeRate(
            @PathVariable UUID id,
            @RequestBody UpdateExchangeRateRequest request) {

        return exchangeRateService.updateExchangeRate(id, request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate updated successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @GetMapping("/{from}/{to}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> getExchangeRateByCurrencies(
            @PathVariable String from,
            @PathVariable String to) {

        return exchangeRateService.getExchangeRateByCurrencies(from, to)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate retrieved successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @PostMapping("/convert")
    public Mono<ResponseEntity<GenericResponse<ConversionResponse>>> convertCurrency(@RequestBody ConversionRequest request) {
        return exchangeRateService.convertCurrency(request)
                .map(result -> ResponseEntity.ok(GenericResponse.success("Conversion performed successfully", result)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}
