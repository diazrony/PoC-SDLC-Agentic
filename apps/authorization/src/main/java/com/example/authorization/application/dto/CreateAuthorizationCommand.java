package com.example.authorization.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreateAuthorizationCommand(String label, BigDecimal limitAmount) {
}
