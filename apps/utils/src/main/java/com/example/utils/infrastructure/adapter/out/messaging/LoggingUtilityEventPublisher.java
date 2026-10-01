package com.example.utils.infrastructure.adapter.out.messaging;

import com.example.utils.application.port.out.UtilityEventPublisherPort;
import com.example.utils.domain.model.Utility;
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
public class LoggingUtilityEventPublisher implements UtilityEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(LoggingUtilityEventPublisher.class);

    @Override
    public void publishCreated(Utility aggregate) {
        LOG.info(StructuredLog.event("utils.created")
                .with("aggregateId", aggregate.id().value())
                .with("status", aggregate.status())
                .format());
    }
}
