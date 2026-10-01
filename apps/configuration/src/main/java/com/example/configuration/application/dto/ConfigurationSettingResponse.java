package com.example.configuration.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Contrato de salida de los casos de uso.
 *
 * <p>Deliberadamente distinto del agregado de dominio: permite evolucionar el
 * modelo interno sin romper a los consumidores de la API.</p>
 */
public record ConfigurationSettingResponse(
        String id,
        String label,
        String status,
        BigDecimal numericValue,
        Instant createdAt) {
}
