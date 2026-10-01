package com.example.retiros.infrastructure.adapter.out.messaging;

import com.example.retiros.application.port.out.WithdrawalEventPublisherPort;
import com.example.retiros.domain.model.Withdrawal;
import com.example.monorepo.events.contract.DomainEvent;
import com.example.monorepo.events.contract.WithdrawalRequestedEvent;
import com.example.monorepo.observability.log.StructuredLog;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

/**
 * ADAPTADOR DE SALIDA de mensajeria.
 *
 * <p>Es el UNICO punto de la aplicacion que conoce el contrato compartido de
 * {@code lib-events}. El modelo de dominio se traduce aqui al contrato de
 * integracion, igual que el mapper de persistencia lo traduce a JPA.</p>
 *
 * <p>En la PoC el evento solo se registra en el log. En produccion este mismo
 * adaptador escribiria en Kafka o en una tabla de outbox, sin que cambiara
 * una sola linea del dominio ni de la capa de aplicacion.</p>
 */
@ApplicationScoped
public class WithdrawalEventPublisherAdapter implements WithdrawalEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(WithdrawalEventPublisherAdapter.class);

    @Override
    public void publishCreated(Withdrawal aggregate) {
        DomainEvent event = new WithdrawalRequestedEvent(
                aggregate.id().value(),
                aggregate.amount(),
                "ATM",
                aggregate.createdAt());

        LOG.info(StructuredLog.event("event.published")
                .with("eventType", event.eventType())
                .with("eventId", event.eventId())
                .with("aggregateId", event.aggregateId())
                .with("schemaVersion", event.schemaVersion())
                .format());
    }
}
