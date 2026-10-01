package com.example.retiros.infrastructure.config;

import com.example.retiros.application.port.out.LoadWithdrawalPort;
import com.example.retiros.application.port.out.SaveWithdrawalPort;
import com.example.retiros.application.port.out.WithdrawalEventPublisherPort;
import com.example.retiros.application.service.WithdrawalService;
import com.example.retiros.domain.service.WithdrawalPolicy;
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
public class WithdrawalBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public WithdrawalPolicy withdrawalPolicy() {
        return new WithdrawalPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetWithdrawalUseCase} y {@code CreateWithdrawalUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public WithdrawalService withdrawalService(LoadWithdrawalPort loadWithdrawalPort,
                                              SaveWithdrawalPort saveWithdrawalPort,
                                              WithdrawalEventPublisherPort eventPublisherPort,
                                              WithdrawalPolicy policy,
                                              TimeProvider timeProvider) {
        return new WithdrawalService(loadWithdrawalPort, saveWithdrawalPort,
                eventPublisherPort, policy, timeProvider);
    }
}
