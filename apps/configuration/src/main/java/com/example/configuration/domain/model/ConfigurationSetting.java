package com.example.configuration.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.ConfigurationSettingEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class ConfigurationSetting {

    private final ConfigurationSettingId id;
    private final String label;
    private final ConfigurationSettingStatus status;
    private final BigDecimal numericValue;
    private final Instant createdAt;

    public ConfigurationSetting(ConfigurationSettingId id,
                      String label,
                      ConfigurationSettingStatus status,
                      BigDecimal numericValue,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.numericValue = Preconditions.requireNonNull(numericValue, "numericValue");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public ConfigurationSettingId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public ConfigurationSettingStatus status() {
        return status;
    }

    public BigDecimal numericValue() {
        return numericValue;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == ConfigurationSettingStatus.BLOCKED;
    }
}
