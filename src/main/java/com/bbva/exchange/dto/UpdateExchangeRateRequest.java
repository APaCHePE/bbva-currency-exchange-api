package com.bbva.exchange.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateExchangeRateRequest {
    private BigDecimal rate;
}