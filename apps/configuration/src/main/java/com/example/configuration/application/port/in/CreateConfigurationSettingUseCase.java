package com.example.configuration.application.port.in;

import com.example.configuration.application.dto.CreateConfigurationSettingCommand;
import com.example.configuration.application.dto.ConfigurationSettingResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreateConfigurationSettingUseCase {

    ConfigurationSettingResponse create(CreateConfigurationSettingCommand command);
}
