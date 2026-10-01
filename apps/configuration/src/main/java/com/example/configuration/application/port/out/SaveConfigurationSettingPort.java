package com.example.configuration.application.port.out;

import com.example.configuration.domain.model.ConfigurationSetting;

/** PUERTO DE SALIDA de escritura. */
public interface SaveConfigurationSettingPort {

    ConfigurationSetting save(ConfigurationSetting aggregate);
}
