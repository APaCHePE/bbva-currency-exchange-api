package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Schema(description = "Respuesta de la conversión de moneda")
public class ConversionResponse {

    @Schema(description = "Monto original antes de la conversión", example = "100.00")
    private BigDecimal originalAmount;

    @Schema(description = "Monto convertido después de aplicar el tipo de cambio", example = "398.00")
    private BigDecimal convertedAmount;

    @Schema(description = "Tasa de cambio aplicada", example = "3.98")
    private BigDecimal rate;
}