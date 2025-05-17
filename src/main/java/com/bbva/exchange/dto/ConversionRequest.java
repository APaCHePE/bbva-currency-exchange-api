package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Petición para convertir un monto entre dos monedas")
public class ConversionRequest {

    @Schema(description = "Moneda de origen", example = "USD")
    private String from;

    @Schema(description = "Moneda de destino", example = "PEN")
    private String to;

    @Schema(description = "Monto a convertir", example = "100.00")
    private BigDecimal amount;
}