package com.bbva.exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta estándar del API")
public class GenericResponse<T> {
    @Schema(description = "Indica si la operación fue exitosa", example = "true")
    private boolean success;
    @Schema(description = "Mensaje descriptivo de la operación", example = "Operación exitosa")
    private String message;
    @Schema(description = "Datos de la respuesta")
    private T data;

    public static <T> GenericResponse<T> success(String message, T data) {
        return new GenericResponse<>(true, message, data);
    }

    public static <T> GenericResponse<T> error(String message) {
        return new GenericResponse<>(false, message, null);
    }

}