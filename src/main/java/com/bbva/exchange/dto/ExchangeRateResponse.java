package com.bbva.exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ExchangeRateResponse {
    private UUID id;
    private String fromCurrency;
    private String toCurrency;
    private BigDecimal rate;
    private LocalDateTime updatedAt;
}
