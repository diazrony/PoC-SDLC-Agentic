package com.example.utils.infrastructure.config;

import com.example.utils.application.port.out.LoadUtilityPort;
import com.example.utils.application.port.out.SaveUtilityPort;
import com.example.utils.application.port.out.UtilityEventPublisherPort;
import com.example.utils.application.service.UtilityService;
import com.example.utils.domain.service.UtilityPolicy;
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
public class UtilityBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public UtilityPolicy utilityPolicy() {
        return new UtilityPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetUtilityUseCase} y {@code CreateUtilityUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public UtilityService utilityService(LoadUtilityPort loadUtilityPort,
                                              SaveUtilityPort saveUtilityPort,
                                              UtilityEventPublisherPort eventPublisherPort,
                                              UtilityPolicy policy,
                                              TimeProvider timeProvider) {
        return new UtilityService(loadUtilityPort, saveUtilityPort,
                eventPublisherPort, policy, timeProvider);
    }
}
