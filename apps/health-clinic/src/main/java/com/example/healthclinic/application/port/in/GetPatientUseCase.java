package com.example.healthclinic.application.port.in;

import com.example.healthclinic.application.dto.PatientResponse;
import com.example.healthclinic.domain.model.PatientId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetPatientUseCase {

    PatientResponse getById(PatientId id);
}
