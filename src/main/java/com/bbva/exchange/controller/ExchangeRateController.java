package com.bbva.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.bbva.exchange.dto.*;
import com.bbva.exchange.service.ExchangeRateService;

@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
@Tag(name = "Tipos de Cambio", description = "Operaciones para consultar, crear, actualizar y convertir tipos de cambio")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    @Operation(
        summary = "Listar todos los tipos de cambio disponibles",
        description = "Recupera una lista de todos los tipos de cambio registrados en el sistema.",
        responses = @ApiResponse(responseCode = "200", description = "Tipos de cambio obtenidos exitosamente")
    )
    @GetMapping
    public Mono<ResponseEntity<GenericResponse<List<ExchangeRateResponse>>>> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates()
                .collectList()
                .map(rates -> ResponseEntity.ok(GenericResponse.success("Exchange rates retrieved successfully", rates)));
    }
    @Operation(
        summary = "Registrar un nuevo tipo de cambio",
        description = "Registra un nuevo tipo de cambio entre dos monedas.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del nuevo tipo de cambio",
            required = true,
            content = @Content(schema = @Schema(implementation = ExchangeRateRequest.class))
        ),
        responses = @ApiResponse(responseCode = "200", description = "Tipo de cambio registrado exitosamente")
    )
    @PostMapping
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> createExchangeRate(
            @RequestBody ExchangeRateRequest request) {

        return exchangeRateService.createExchangeRate(request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate registered successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
        summary = "Actualizar un tipo de cambio existente",
        description = "Actualiza el tipo de cambio entre dos monedas identificadas por su ID.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos actualizados del tipo de cambio",
            required = true,
            content = @Content(schema = @Schema(implementation = UpdateExchangeRateRequest.class))
        ),
        responses = @ApiResponse(responseCode = "200", description = "Tipo de cambio actualizado exitosamente")
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> updateExchangeRate(
            @PathVariable UUID id,
            @RequestBody UpdateExchangeRateRequest request) {

        return exchangeRateService.updateExchangeRate(id, request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate updated successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
        summary = "Obtener el tipo de cambio entre dos monedas",
        description = "Recupera el tipo de cambio registrado entre dos monedas específicas.",
        responses = @ApiResponse(responseCode = "200", description = "Tipo de cambio obtenido exitosamente")
    )
    @GetMapping("/{from}/{to}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> getExchangeRateByCurrencies(
            @PathVariable String from,
            @PathVariable String to) {

        return exchangeRateService.getExchangeRateByCurrencies(from, to)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate retrieved successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
        summary = "Convertir un monto entre dos monedas",
        description = "Permite convertir un monto de una moneda a otra usando el tipo de cambio registrado.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para la conversión de moneda",
            required = true,
            content = @Content(schema = @Schema(implementation = ConversionRequest.class))
        ),
        responses = @ApiResponse(responseCode = "200", description = "Conversión realizada exitosamente")
    )
    @PostMapping("/convert")
    public Mono<ResponseEntity<GenericResponse<ConversionResponse>>> convertCurrency(@RequestBody ConversionRequest request) {
        return exchangeRateService.convertCurrency(request)
                .map(result -> ResponseEntity.ok(GenericResponse.success("Conversion performed successfully", result)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}
