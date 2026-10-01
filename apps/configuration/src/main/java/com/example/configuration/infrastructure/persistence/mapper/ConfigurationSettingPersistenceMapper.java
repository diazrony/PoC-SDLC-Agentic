package com.example.configuration.infrastructure.persistence.mapper;

import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.configuration.domain.model.ConfigurationSettingStatus;
import com.example.configuration.infrastructure.persistence.entity.ConfigurationSettingEntity;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Frontera explicita entre el modelo de dominio y el modelo de persistencia.
 *
 * <pre>
 *   Domain Model  --mapper--&gt;  JPA Entity
 *   JPA Entity    --mapper--&gt;  Domain Model
 * </pre>
 */
@ApplicationScoped
public class ConfigurationSettingPersistenceMapper {

    public ConfigurationSetting toDomain(ConfigurationSettingEntity entity) {
        return new ConfigurationSetting(
                ConfigurationSettingId.of(entity.id),
                entity.label,
                ConfigurationSettingStatus.valueOf(entity.status),
                entity.numericValue,
                entity.createdAt);
    }

    public ConfigurationSettingEntity toEntity(ConfigurationSetting aggregate) {
        ConfigurationSettingEntity entity = new ConfigurationSettingEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.numericValue = aggregate.numericValue();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
