package com.example.transaction.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreateTransactionCommand(String label, BigDecimal amount) {
}
