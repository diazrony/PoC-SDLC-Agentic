package com.example.configuration.application.service;

import com.example.configuration.application.dto.CreateConfigurationSettingCommand;
import com.example.configuration.application.dto.ConfigurationSettingResponse;
import com.example.configuration.application.mapper.ConfigurationSettingDtoMapper;
import com.example.configuration.application.port.in.CreateConfigurationSettingUseCase;
import com.example.configuration.application.port.in.GetConfigurationSettingUseCase;
import com.example.configuration.application.port.out.LoadConfigurationSettingPort;
import com.example.configuration.application.port.out.SaveConfigurationSettingPort;
import com.example.configuration.application.port.out.ConfigurationSettingEventPublisherPort;
import com.example.configuration.domain.exception.ConfigurationSettingNotFoundException;
import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.configuration.domain.model.ConfigurationSettingStatus;
import com.example.configuration.domain.service.ConfigurationSettingPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.ConfigurationSettingBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class ConfigurationSettingService implements GetConfigurationSettingUseCase, CreateConfigurationSettingUseCase {

    private final LoadConfigurationSettingPort loadConfigurationSettingPort;
    private final SaveConfigurationSettingPort saveConfigurationSettingPort;
    private final ConfigurationSettingEventPublisherPort eventPublisherPort;
    private final ConfigurationSettingPolicy policy;
    private final TimeProvider timeProvider;

    public ConfigurationSettingService(LoadConfigurationSettingPort loadConfigurationSettingPort,
                             SaveConfigurationSettingPort saveConfigurationSettingPort,
                             ConfigurationSettingEventPublisherPort eventPublisherPort,
                             ConfigurationSettingPolicy policy,
                             TimeProvider timeProvider) {
        this.loadConfigurationSettingPort = loadConfigurationSettingPort;
        this.saveConfigurationSettingPort = saveConfigurationSettingPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public ConfigurationSettingResponse getById(ConfigurationSettingId id) {
        ConfigurationSetting aggregate = loadConfigurationSettingPort.loadById(id)
                .orElseThrow(() -> new ConfigurationSettingNotFoundException(id));
        policy.ensureUsable(aggregate);
        return ConfigurationSettingDtoMapper.toResponse(aggregate);
    }

    @Override
    public ConfigurationSettingResponse create(CreateConfigurationSettingCommand command) {
        policy.validateForCreation(command.label(), command.numericValue());

        ConfigurationSetting aggregate = new ConfigurationSetting(
                ConfigurationSettingId.of(Ids.newId()),
                command.label(),
                ConfigurationSettingStatus.ACTIVE,
                command.numericValue(),
                timeProvider.now());

        ConfigurationSetting saved = saveConfigurationSettingPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return ConfigurationSettingDtoMapper.toResponse(saved);
    }
}
