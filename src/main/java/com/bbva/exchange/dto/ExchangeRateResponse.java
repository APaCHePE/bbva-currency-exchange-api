package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta que representa un tipo de cambio registrado en el sistema")
public class ExchangeRateResponse {

    @Schema(description = "Identificador único del tipo de cambio", example = "c4c8ebf7-340b-4783-8aee-871506ad1391")
    private UUID id;

    @Schema(description = "Moneda de origen", example = "USD")
    private String fromCurrency;

    @Schema(description = "Moneda de destino", example = "PEN")
    private String toCurrency;

    @Schema(description = "Tasa de cambio", example = "3.98")
    private BigDecimal rate;

    @Schema(description = "Fecha y hora de la última actualización", example = "2025-05-16T12:30:04")
    private LocalDateTime updatedAt;
}