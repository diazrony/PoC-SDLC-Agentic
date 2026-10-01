package com.example.healthclinic.infrastructure.adapter.out.persistence;

import com.example.healthclinic.application.port.out.LoadPatientPort;
import com.example.healthclinic.application.port.out.SavePatientPort;
import com.example.healthclinic.domain.model.Patient;
import com.example.healthclinic.domain.model.PatientId;
import com.example.healthclinic.infrastructure.persistence.entity.PatientEntity;
import com.example.healthclinic.infrastructure.persistence.mapper.PatientPersistenceMapper;
import com.example.healthclinic.infrastructure.persistence.repository.PatientJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

/**
 * ADAPTADOR DE SALIDA (driven adapter): implementa los puertos de persistencia.
 *
 * <p>Aqui, y solo aqui, aparecen JPA y las transacciones.</p>
 */
@ApplicationScoped
public class PatientPersistenceAdapter implements LoadPatientPort, SavePatientPort {

    private final PatientJpaRepository repository;
    private final PatientPersistenceMapper mapper;

    @Inject
    public PatientPersistenceAdapter(PatientJpaRepository repository,
                                        PatientPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Patient> loadById(PatientId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Patient save(Patient aggregate) {
        PatientEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
