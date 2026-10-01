package com.example.healthclinic.infrastructure.config;

import com.example.healthclinic.application.port.out.LoadPatientPort;
import com.example.healthclinic.application.port.out.SavePatientPort;
import com.example.healthclinic.application.port.out.PatientEventPublisherPort;
import com.example.healthclinic.application.service.PatientService;
import com.example.healthclinic.domain.service.PatientPolicy;
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
public class PatientBeanConfiguration {

    @Produces
    @ApplicationScoped
    public TimeProvider timeProvider() {
        return TimeProvider.system();
    }

    @Produces
    @ApplicationScoped
    public PatientPolicy patientPolicy() {
        return new PatientPolicy();
    }

    /**
     * Publica el servicio de aplicacion. CDI expone automaticamente todos los
     * tipos del bean, de modo que los puertos de entrada
     * {@code GetPatientUseCase} y {@code CreatePatientUseCase} quedan
     * disponibles para el adaptador REST.
     */
    @Produces
    @ApplicationScoped
    public PatientService patientService(LoadPatientPort loadPatientPort,
                                              SavePatientPort savePatientPort,
                                              PatientEventPublisherPort eventPublisherPort,
                                              PatientPolicy policy,
                                              TimeProvider timeProvider) {
        return new PatientService(loadPatientPort, savePatientPort,
                eventPublisherPort, policy, timeProvider);
    }
}
