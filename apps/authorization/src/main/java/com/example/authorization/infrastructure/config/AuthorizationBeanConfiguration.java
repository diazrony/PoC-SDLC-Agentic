package com.example.authorization.infrastructure.config;

import com.example.authorization.application.port.out.LoadAuthorizationPort;
import com.example.authorization.application.port.out.SaveAuthorizationPort;
import com.example.authorization.application.port.out.AuthorizationEventPublisherPort;
import com.example.authorization.application.service.AuthorizationService;
import com.example.authorization.domain.service.AuthorizationPolicy;
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
public class AuthorizationBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public AuthorizationPolicy authorizationPolicy() {
        return new AuthorizationPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetAuthorizationUseCase} y {@code CreateAuthorizationUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public AuthorizationService authorizationService(LoadAuthorizationPort loadAuthorizationPort,
                                              SaveAuthorizationPort saveAuthorizationPort,
                                              AuthorizationEventPublisherPort eventPublisherPort,
                                              AuthorizationPolicy policy,
                                              TimeProvider timeProvider) {
        return new AuthorizationService(loadAuthorizationPort, saveAuthorizationPort,
                eventPublisherPort, policy, timeProvider);
    }
}
