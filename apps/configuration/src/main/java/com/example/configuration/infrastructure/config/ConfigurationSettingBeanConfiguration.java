package com.example.configuration.infrastructure.config;

import com.example.configuration.application.port.out.LoadConfigurationSettingPort;
import com.example.configuration.application.port.out.SaveConfigurationSettingPort;
import com.example.configuration.application.port.out.ConfigurationSettingEventPublisherPort;
import com.example.configuration.application.service.ConfigurationSettingService;
import com.example.configuration.domain.service.ConfigurationSettingPolicy;
import com.example.monorepo.core.time.TimeProvider;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * Punto de union entre el hexagono y el contenedor CDI.
 *
 * <p>Esta clase es la razon por la que dominio y aplicacion pueden ser Java
 * puro: toda la dependencia hacia Quarkus/CDI esta concentrada aqui.</p>
 */
@ApplicationScoped
public class ConfigurationSettingBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public ConfigurationSettingPolicy configurationSettingPolicy() {
        return new ConfigurationSettingPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetConfigurationSettingUseCase} y {@code CreateConfigurationSettingUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public ConfigurationSettingService configurationSettingService(LoadConfigurationSettingPort loadConfigurationSettingPort,
                                              SaveConfigurationSettingPort saveConfigurationSettingPort,
                                              ConfigurationSettingEventPublisherPort eventPublisherPort,
                                              ConfigurationSettingPolicy policy,
                                              TimeProvider timeProvider) {
        return new ConfigurationSettingService(loadConfigurationSettingPort, saveConfigurationSettingPort,
                eventPublisherPort, policy, timeProvider);
    }
}
