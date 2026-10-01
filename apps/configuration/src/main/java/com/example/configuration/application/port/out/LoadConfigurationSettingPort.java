package com.example.configuration.application.port.out;

import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.configuration.domain.model.ConfigurationSettingId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadConfigurationSettingPort {

    Optional<ConfigurationSetting> loadById(ConfigurationSettingId id);
}
