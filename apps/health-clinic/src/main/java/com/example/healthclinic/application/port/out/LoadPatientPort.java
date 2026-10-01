package com.example.healthclinic.application.port.out;

import com.example.healthclinic.domain.model.Patient;
import com.example.healthclinic.domain.model.PatientId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadPatientPort {

    Optional<Patient> loadById(PatientId id);
}
