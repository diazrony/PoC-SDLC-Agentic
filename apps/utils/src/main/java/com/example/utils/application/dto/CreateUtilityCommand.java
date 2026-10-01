package com.example.utils.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreateUtilityCommand(String label, BigDecimal threshold) {
}
