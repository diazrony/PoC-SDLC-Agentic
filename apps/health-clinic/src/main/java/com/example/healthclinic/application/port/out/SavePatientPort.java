package com.example.healthclinic.application.port.out;

import com.example.healthclinic.domain.model.Patient;

/** PUERTO DE SALIDA de escritura. */
public interface SavePatientPort {

    Patient save(Patient aggregate);
}
