package com.example.healthclinic.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreatePatientCommand(String label, BigDecimal copaymentAmount) {
}
