package com.bbva.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ConversionResponse {
    private BigDecimal originalAmount;
    private BigDecimal convertedAmount;
    private BigDecimal rate;
}