package com.example.configuration.application.mapper;

import com.example.configuration.application.dto.ConfigurationSettingResponse;
import com.example.configuration.domain.model.ConfigurationSetting;

/** Conversion dominio -> DTO de salida. */
public final class ConfigurationSettingDtoMapper {

    private ConfigurationSettingDtoMapper() {
    }

    public static ConfigurationSettingResponse toResponse(ConfigurationSetting aggregate) {
        return new ConfigurationSettingResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.numericValue(),
                aggregate.createdAt());
    }
}
