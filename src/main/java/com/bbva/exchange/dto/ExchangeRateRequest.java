package com.bbva.exchange.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeRateRequest {
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
}