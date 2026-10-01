package com.example.healthclinic.application.mapper;

import com.example.healthclinic.application.dto.PatientResponse;
import com.example.healthclinic.domain.model.Patient;

/** Conversion dominio -> DTO de salida. */
public final class PatientDtoMapper {

    private PatientDtoMapper() {
    }

    public static PatientResponse toResponse(Patient aggregate) {
        return new PatientResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.copaymentAmount(),
                aggregate.createdAt());
    }
}
