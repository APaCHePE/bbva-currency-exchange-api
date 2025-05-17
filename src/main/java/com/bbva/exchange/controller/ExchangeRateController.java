package com.bbva.exchange.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.bbva.exchange.dto.*;
import com.bbva.exchange.service.ExchangeRateService;

@RestController
@RequestMapping("/exchange-rates")
@RequiredArgsConstructor
@Tag(name = "Tipos de Cambio", description = "Operaciones para consultar, crear, actualizar y convertir tipos de cambio")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;
    @Operation(
      summary = "Listar todos los tipos de cambio disponibles",
      description = "Recupera una lista de todos los tipos de cambio registrados en el sistema.",
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Tipos de cambio obtenidos exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Get All Exchange Rates Example",
              value = """
                        {
                            "success": true,
                            "message": "Exchange rates retrieved successfully",
                            "data": [
                                {
                                    "id": "4a5f7790-2eff-4297-a228-baeeeffe9744",
                                    "fromCurrency": "USD",
                                    "toCurrency": "PEN",
                                    "rate": 3.9800,
                                    "updatedAt": "2025-05-17T00:00:00"
                                },
                                {
                                    "id": "6b377a4b-e1b3-4d5a-9b9f-1ec51d549513",
                                    "fromCurrency": "EUR",
                                    "toCurrency": "USD",
                                    "rate": 1.1000,
                                    "updatedAt": "2025-05-17T00:00:00"
                                }
                            ]
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @GetMapping
    public Mono<ResponseEntity<GenericResponse<List<ExchangeRateResponse>>>> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates()
                .collectList()
                .map(rates -> ResponseEntity.ok(GenericResponse.success("Exchange rates retrieved successfully", rates)));
    }
    @Operation(
      summary = "Registrar un nuevo tipo de cambio",
      description = "Registra un nuevo tipo de cambio entre dos monedas.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos del nuevo tipo de cambio",
        required = true,
        content = @Content(
          schema = @Schema(implementation = ExchangeRateRequest.class),
          examples = @ExampleObject(
            name = "Create Example",
            value = """
                    {
                        "fromCurrency": "USD",
                        "toCurrency": "PEN",
                        "rate": 3.9800
                    }
                    """
          )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Tipo de cambio registrado exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Create Success Example",
              value = """
                        {
                            "success": true,
                            "message": "Exchange rate registered successfully",
                            "data": {
                                "id": "4a5f7790-2eff-4297-a228-baeeeffe9744",
                                "fromCurrency": "USD",
                                "toCurrency": "PEN",
                                "rate": 3.9800,
                                "updatedAt": "2025-05-17T00:00:00"
                            }
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error al registrar el tipo de cambio",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Create Error Example",
              value = """
                        {
                            "success": false,
                            "message": "Exchange rate already exists",
                            "data": null
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @PostMapping
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> createExchangeRate(
            @RequestBody ExchangeRateRequest request) {

        return exchangeRateService.createExchangeRate(request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate registered successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
      summary = "Actualizar un tipo de cambio existente",
      description = "Actualiza el tipo de cambio entre dos monedas identificadas por su ID.",
      parameters = {
        @Parameter(
          name = "id",
          description = "Identificador único del tipo de cambio a actualizar",
          required = true,
          example = "4a5f7790-2eff-4297-a228-baeeeffe9744"
        )
      },
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos actualizados del tipo de cambio",
        required = true,
        content = @Content(
          schema = @Schema(implementation = UpdateExchangeRateRequest.class),
          examples = @ExampleObject(
            name = "Update Example",
            value = """
                    {
                        "rate": 4.1000
                    }
                    """
          )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Tipo de cambio actualizado exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Update Success Example",
              value = """
                        {
                            "success": true,
                            "message": "Exchange rate updated successfully",
                            "data": {
                                "id": "4a5f7790-2eff-4297-a228-baeeeffe9744",
                                "fromCurrency": "USD",
                                "toCurrency": "PEN",
                                "rate": 4.1000,
                                "updatedAt": "2025-05-17T00:00:00"
                            }
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error al actualizar el tipo de cambio",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Update Error Example",
              value = """
                        {
                            "success": false,
                            "message": "Exchange rate with ID not found",
                            "data": null
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @PutMapping("/{id}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> updateExchangeRate(
            @PathVariable UUID id,
            @RequestBody UpdateExchangeRateRequest request) {

        return exchangeRateService.updateExchangeRate(id, request)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate updated successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
      summary = "Obtener el tipo de cambio entre dos monedas",
      description = "Recupera el tipo de cambio registrado entre dos monedas específicas.",
      parameters = {
        @Parameter(
          name = "from",
          description = "Código de la moneda de origen (ej. USD)",
          required = true,
          example = "USD"
        ),
        @Parameter(
          name = "to",
          description = "Código de la moneda de destino (ej. PEN)",
          required = true,
          example = "PEN"
        )
      },
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Tipo de cambio obtenido exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Exchange Rate Success Example",
              value = """
                        {
                            "success": true,
                            "message": "Exchange rate retrieved successfully",
                            "data": {
                                "id": "4a5f7790-2eff-4297-a228-baeeeffe9744",
                                "fromCurrency": "USD",
                                "toCurrency": "PEN",
                                "rate": 3.9800,
                                "updatedAt": "2025-05-16T06:53:06.923679"
                            }
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error al obtener el tipo de cambio",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Exchange Rate Error Example",
              value = """
                        {
                            "success": false,
                            "message": "Exchange rate not found for USD to PEN",
                            "data": null
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @GetMapping("/{from}/{to}")
    public Mono<ResponseEntity<GenericResponse<ExchangeRateResponse>>> getExchangeRateByCurrencies(
            @PathVariable String from,
            @PathVariable String to) {

        return exchangeRateService.getExchangeRateByCurrencies(from, to)
                .map(rate -> ResponseEntity.ok(GenericResponse.success("Exchange rate retrieved successfully", rate)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
    @Operation(
      summary = "Convertir un monto entre dos monedas",
      description = "Permite convertir un monto de una moneda a otra usando el tipo de cambio registrado.",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Datos para la conversión de moneda",
        required = true,
        content = @Content(
          schema = @Schema(implementation = ConversionRequest.class),
          examples = @ExampleObject(
            name = "Conversion Example",
            value = """
                    {
                        "from": "USD",
                        "to": "PEN",
                        "amount": 100.00
                    }
                    """
          )
        )
      ),
      responses = {
        @ApiResponse(
          responseCode = "200",
          description = "Conversión realizada exitosamente",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Conversion Success Example",
              value = """
                        {
                            "success": true,
                            "message": "Conversion performed successfully",
                            "data": {
                                "originalAmount": 100.00,
                                "convertedAmount": 398.00,
                                "rate": 3.98
                            }
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "400",
          description = "Error de validación o conversión",
          content = @Content(
            schema = @Schema(implementation = GenericResponse.class),
            examples = @ExampleObject(
              name = "Conversion Error Example",
              value = """
                        {
                            "success": false,
                            "message": "Invalid currency or rate not found",
                            "data": null
                        }
                        """
            )
          )
        ),
        @ApiResponse(
          responseCode = "500",
          description = "Error interno del servidor",
          content = @Content(schema = @Schema(implementation = GenericResponse.class))
        )
      }
    )
    @PostMapping("/convert")
    public Mono<ResponseEntity<GenericResponse<ConversionResponse>>> convertCurrency(@RequestBody ConversionRequest request) {
        return exchangeRateService.convertCurrency(request)
                .map(result -> ResponseEntity.ok(GenericResponse.success("Conversion performed successfully", result)))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(GenericResponse.error(e.getMessage()))));
    }
}
