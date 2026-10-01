package com.example.healthclinic.application.port.in;

import com.example.healthclinic.application.dto.CreatePatientCommand;
import com.example.healthclinic.application.dto.PatientResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreatePatientUseCase {

    PatientResponse create(CreatePatientCommand command);
}
