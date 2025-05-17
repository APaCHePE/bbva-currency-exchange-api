package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Petición para registrar un nuevo tipo de cambio")
public class ExchangeRateRequest {

    @Schema(description = "Moneda de origen", example = "USD")
    private String fromCurrency;

    @Schema(description = "Moneda de destino", example = "PEN")
    private String toCurrency;

    @Schema(description = "Tasa de cambio", example = "3.98")
    private BigDecimal rate;
}