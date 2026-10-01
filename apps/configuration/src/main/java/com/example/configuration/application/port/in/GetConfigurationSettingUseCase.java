package com.example.configuration.application.port.in;

import com.example.configuration.application.dto.ConfigurationSettingResponse;
import com.example.configuration.domain.model.ConfigurationSettingId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetConfigurationSettingUseCase {

    ConfigurationSettingResponse getById(ConfigurationSettingId id);
}
