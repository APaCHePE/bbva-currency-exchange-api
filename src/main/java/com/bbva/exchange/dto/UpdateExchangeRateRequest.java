package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Petición para actualizar la tasa de cambio de una moneda existente")
public class UpdateExchangeRateRequest {

    @Schema(description = "Nueva tasa de cambio", example = "4.10")
    private BigDecimal rate;
}