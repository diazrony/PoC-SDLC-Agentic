package com.example.configuration.domain.exception;

import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class ConfigurationSettingBlockedException extends BusinessException {

    public ConfigurationSettingBlockedException(ConfigurationSettingId id) {
        super(ConfigurationSettingErrorCode.CONF_BLOCKED,
                "El ConfigurationSetting %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
