package com.example.healthclinic.infrastructure.persistence.repository;

import com.example.healthclinic.infrastructure.persistence.entity.PatientEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadPatientPort} y {@code SavePatientPort}.</p>
 */
@ApplicationScoped
public class PatientJpaRepository implements PanacheRepositoryBase<PatientEntity, String> {
}
