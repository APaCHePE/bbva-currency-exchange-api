package com.bbva.exchange.service;

import com.bbva.exchange.dto.*;
import com.bbva.exchange.exception.BusinessException;
import com.bbva.exchange.model.ExchangeRate;
import com.bbva.exchange.repository.ExchangeRateRepository;
import com.bbva.exchange.util.AppConstants;
import com.bbva.exchange.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

  private final ExchangeRateRepository exchangeRateRepository;
  private final AuditLogService auditLogService;

  public Flux<ExchangeRateResponse> getAllExchangeRates() {
    return exchangeRateRepository.findAll().switchIfEmpty(Flux.error(new BusinessException("No exchange rates found"))).map(rate -> new ExchangeRateResponse(rate.getId(), rate.getFromCurrency(), rate.getToCurrency(), rate.getRate(), rate.getUpdatedAt())).onErrorMap(e -> new BusinessException("Error retrieving exchange rates: " + e.getMessage()));
  }

  public Mono<ExchangeRateResponse> createExchangeRate(ExchangeRateRequest request) {
    return SecurityUtil.requireAdmin().then(ReactiveSecurityContextHolder.getContext()).flatMap(context -> {
      String username = context.getAuthentication().getName();
      UUID userId = (UUID) context.getAuthentication().getCredentials();
      System.out.println("Username: " + username);
      ExchangeRate rate = new ExchangeRate();
      rate.setFromCurrency(request.getFromCurrency());
      rate.setToCurrency(request.getToCurrency());
      rate.setRate(request.getRate());
      rate.setUpdatedAt(LocalDateTime.now());

      return exchangeRateRepository.save(rate).flatMap(saved -> auditLogService.registerAudit(userId, username, "CREATE_EXCHANGE_RATE", String.format("Created exchange rate from %s to %s at rate %s (ID: %s)", saved.getFromCurrency(), saved.getToCurrency(), saved.getRate(), saved.getId())).thenReturn(new ExchangeRateResponse(saved.getId(), saved.getFromCurrency(), saved.getToCurrency(), saved.getRate(), saved.getUpdatedAt())));
    });
  }

  public Mono<ExchangeRateResponse> updateExchangeRate(UUID id, UpdateExchangeRateRequest request) {
    return SecurityUtil.requireAdmin().then(ReactiveSecurityContextHolder.getContext()).flatMap(context -> {
      String username = context.getAuthentication().getName();
      UUID userId = (UUID) context.getAuthentication().getCredentials();
      return exchangeRateRepository.findById(id).switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found"))).flatMap(existing -> {
        existing.setRate(request.getRate());
        existing.setUpdatedAt(LocalDateTime.now());
        return exchangeRateRepository.save(existing).flatMap(updated -> auditLogService.registerAudit(userId, username, "UPDATE_EXCHANGE_RATE", String.format("Updated exchange rate from %s to %s to rate %s (ID: %s)", updated.getFromCurrency(), updated.getToCurrency(), updated.getRate(), updated.getId())).thenReturn(new ExchangeRateResponse(updated.getId(), updated.getFromCurrency(), updated.getToCurrency(), updated.getRate(), updated.getUpdatedAt())));
      });
    });
  }

  public Mono<ExchangeRateResponse> getExchangeRateByCurrencies(String from, String to) {
    return ReactiveSecurityContextHolder.getContext().flatMap(context -> {
      String username = context.getAuthentication().getName();
      UUID userId = (UUID) context.getAuthentication().getCredentials();
      return exchangeRateRepository.findAll().filter(rate -> rate.getFromCurrency().equalsIgnoreCase(from) && rate.getToCurrency().equalsIgnoreCase(to)).next().switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found for " + from + " to " + to))).flatMap(rate -> auditLogService.registerAudit(userId, username, "CONSULT_EXCHANGE_RATE", String.format("Consulted exchange rate from %s to %s with rate %s (ID: %s)", rate.getFromCurrency(), rate.getToCurrency(), rate.getRate(), rate.getId())).thenReturn(new ExchangeRateResponse(rate.getId(), rate.getFromCurrency(), rate.getToCurrency(), rate.getRate(), rate.getUpdatedAt())));
    });
  }

  public Mono<ConversionResponse> convertCurrency(ConversionRequest request) {
    return ReactiveSecurityContextHolder.getContext().flatMap(context -> {
      String username = context.getAuthentication().getName();
      UUID userId = (UUID) context.getAuthentication().getCredentials();
      return exchangeRateRepository.findAll().filter(rate -> rate.getFromCurrency().equalsIgnoreCase(request.getFrom()) && rate.getToCurrency().equalsIgnoreCase(request.getTo())).next().switchIfEmpty(Mono.error(new BusinessException("Exchange rate not found for " + request.getFrom() + " to " + request.getTo()))).flatMap(rate -> {
        BigDecimal convertedAmount = request.getAmount().multiply(rate.getRate());
        ConversionResponse response = new ConversionResponse(request.getAmount(), convertedAmount, rate.getRate());
        return auditLogService.registerAudit(userId, username, "CONVERT_CURRENCY", String.format("Converted %s %s to %s %s using rate %s", request.getAmount(), request.getFrom(), convertedAmount, request.getTo(), rate.getRate())).thenReturn(response);
      });
    });
  }
}