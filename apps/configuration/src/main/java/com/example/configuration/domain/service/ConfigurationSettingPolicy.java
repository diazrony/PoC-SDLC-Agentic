package com.example.configuration.domain.service;

import com.example.configuration.domain.exception.ConfigurationSettingBlockedException;
import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.monorepo.exceptions.ValidationException;

import java.math.BigDecimal;

/**
 * Servicio de dominio: reglas que no pertenecen a un unico agregado.
 *
 * <p>Sin anotaciones. Se publica como bean CDI desde
 * {@code infrastructure.config}, de forma que el dominio permanece puro.</p>
 */
public class ConfigurationSettingPolicy {

    /** Un agregado bloqueado no se puede entregar al exterior. */
    public void ensureUsable(ConfigurationSetting aggregate) {
        if (aggregate.isBlocked()) {
            throw new ConfigurationSettingBlockedException(aggregate.id());
        }
    }

    /** Invariantes de creacion. */
    public void validateForCreation(String label, BigDecimal numericValue) {
        if (label == null || label.isBlank()) {
            throw ValidationException.ofField("label", "es obligatorio");
        }
        if (numericValue == null || numericValue.signum() < 0) {
            throw ValidationException.ofField("numericValue", "no puede ser negativo");
        }
    }
}
