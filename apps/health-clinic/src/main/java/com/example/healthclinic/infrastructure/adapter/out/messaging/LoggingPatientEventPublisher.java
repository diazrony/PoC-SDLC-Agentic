package com.example.healthclinic.infrastructure.adapter.out.messaging;

import com.example.healthclinic.application.port.out.PatientEventPublisherPort;
import com.example.healthclinic.domain.model.Patient;
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
public class LoggingPatientEventPublisher implements PatientEventPublisherPort {

    private static final Logger LOG = Logger.getLogger(LoggingPatientEventPublisher.class);

    @Override
    public void publishCreated(Patient aggregate) {
        LOG.info(StructuredLog.event("health-clinic.created")
                .with("aggregateId", aggregate.id().value())
                .with("status", aggregate.status())
                .format());
    }
}
