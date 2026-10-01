package com.example.configuration.domain.exception;

import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class ConfigurationSettingNotFoundException extends NotFoundException {

    public ConfigurationSettingNotFoundException(ConfigurationSettingId id) {
        super(ConfigurationSettingErrorCode.CONF_NOT_FOUND, "ConfigurationSetting", id.value());
    }
}
