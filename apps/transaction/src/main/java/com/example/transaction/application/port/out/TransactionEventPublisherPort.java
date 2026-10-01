package com.example.transaction.application.port.out;

import com.example.transaction.domain.model.Transaction;

/**
 * PUERTO DE SALIDA de publicacion de eventos.
 *
 * <p>Observa que el puerto habla en terminos de DOMINIO. El contrato de
 * integracion compartido (lib-events) aparece unicamente en el adaptador,
 * que es la frontera real del sistema.</p>
 */
public interface TransactionEventPublisherPort {

    void publishCreated(Transaction aggregate);
}
