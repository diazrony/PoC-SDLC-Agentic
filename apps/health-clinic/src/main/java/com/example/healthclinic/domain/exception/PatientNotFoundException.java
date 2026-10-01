package com.example.healthclinic.domain.exception;

import com.example.healthclinic.domain.model.PatientId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class PatientNotFoundException extends NotFoundException {

    public PatientNotFoundException(PatientId id) {
        super(PatientErrorCode.HCLN_NOT_FOUND, "Patient", id.value());
    }
}
