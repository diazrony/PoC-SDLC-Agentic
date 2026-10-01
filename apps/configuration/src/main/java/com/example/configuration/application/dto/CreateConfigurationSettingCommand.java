package com.example.configuration.application.dto;

import java.math.BigDecimal;

/** Contrato de entrada del caso de uso de creacion. */
public record CreateConfigurationSettingCommand(String label, BigDecimal numericValue) {
}
