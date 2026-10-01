package com.example.retiros.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreateWithdrawalCommand(String label, BigDecimal amount) {
}
