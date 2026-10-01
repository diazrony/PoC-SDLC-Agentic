package com.example.authorization.infrastructure.adapter.out.messaging;

import com.example.authorization.application.port.out.AuthorizationEventPublisherPort;
import com.example.authorization.domain.model.Authorization;
import com.example.monorepo.observability.log.StructuredLog;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

/**
 * ADAPTADOR DE SALIDA de notificacion.
 *
 * <p>Esta aplicacion NO depende de {@code lib-events} a proposito: sus eventos
 * todavia no son un contrato de integracion publico, asi que no tiene sentido
 * pagar el acoplamiento de compartirlo. El dia que otro servicio necesite
 * consumirlos, se promueve el contrato a la libreria y solo cambia esta clase.</p>
 */
@ApplicationScoped
public class LoggingAuthorizationEventPublisher implements AuthorizationEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(LoggingAuthorizationEventPublisher.class);

    @Override
    public void publishCreated(Authorization aggregate) {
        LOG.info(StructuredLog.event("authorization.created")
                .with("aggregateId", aggregate.id().value())
                .with("status", aggregate.status())
                .format());
    }
}
