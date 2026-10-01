package com.example.configuration.domain.model;

import com.example.monorepo.core.util.Preconditions;

/**
 * Identificador del agregado como Value Object.
 *
 * <p>Evita el "obsesion por los tipos primitivos": un String suelto puede ser
 * cualquier cosa, un ConfigurationSettingId solo puede ser esto.</p>
 */
public record ConfigurationSettingId(String value) {

    public ConfigurationSettingId {
        Preconditions.requireNonBlank(value, "ConfigurationSettingId");
    }

    public static ConfigurationSettingId of(String value) {
        return new ConfigurationSettingId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
