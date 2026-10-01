package com.example.healthclinic.infrastructure.persistence.mapper;

import com.example.healthclinic.domain.model.Patient;
import com.example.healthclinic.domain.model.PatientId;
import com.example.healthclinic.domain.model.PatientStatus;
import com.example.healthclinic.infrastructure.persistence.entity.PatientEntity;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Frontera explicita entre el modelo de dominio y el modelo de persistencia.
 *
 * <pre>
 *   Domain Model  --mapper--&gt;  JPA Entity
 *   JPA Entity    --mapper--&gt;  Domain Model
 * </pre>
 */
@ApplicationScoped
public class PatientPersistenceMapper {

    public Patient toDomain(PatientEntity entity) {
        return new Patient(
                PatientId.of(entity.id),
                entity.label,
                PatientStatus.valueOf(entity.status),
                entity.copaymentAmount,
                entity.createdAt);
    }

    public PatientEntity toEntity(Patient aggregate) {
        PatientEntity entity = new PatientEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.copaymentAmount = aggregate.copaymentAmount();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
