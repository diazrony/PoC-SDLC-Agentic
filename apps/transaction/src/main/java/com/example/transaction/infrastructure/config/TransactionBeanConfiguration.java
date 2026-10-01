package com.example.transaction.infrastructure.config;

import com.example.transaction.application.port.out.LoadTransactionPort;
import com.example.transaction.application.port.out.SaveTransactionPort;
import com.example.transaction.application.port.out.TransactionEventPublisherPort;
import com.example.transaction.application.service.TransactionService;
import com.example.transaction.domain.service.TransactionPolicy;
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
public class TransactionBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public TransactionPolicy transactionPolicy() {
        return new TransactionPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetTransactionUseCase} y {@code CreateTransactionUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public TransactionService transactionService(LoadTransactionPort loadTransactionPort,
                                              SaveTransactionPort saveTransactionPort,
                                              TransactionEventPublisherPort eventPublisherPort,
                                              TransactionPolicy policy,
                                              TimeProvider timeProvider) {
        return new TransactionService(loadTransactionPort, saveTransactionPort,
                eventPublisherPort, policy, timeProvider);
    }
}
