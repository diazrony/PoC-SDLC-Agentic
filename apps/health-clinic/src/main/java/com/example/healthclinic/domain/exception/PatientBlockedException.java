package com.example.healthclinic.domain.exception;

import com.example.healthclinic.domain.model.PatientId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class PatientBlockedException extends BusinessException {

    public PatientBlockedException(PatientId id) {
        super(PatientErrorCode.HCLN_BLOCKED,
                "El Patient %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
