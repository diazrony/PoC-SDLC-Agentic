package com.example.utils.infrastructure.adapter.out.persistence;

import com.example.utils.application.port.out.LoadUtilityPort;
import com.example.utils.application.port.out.SaveUtilityPort;
import com.example.utils.domain.model.Utility;
import com.example.utils.domain.model.UtilityId;
import com.example.utils.infrastructure.persistence.entity.UtilityEntity;
import com.example.utils.infrastructure.persistence.mapper.UtilityPersistenceMapper;
import com.example.utils.infrastructure.persistence.repository.UtilityJpaRepository;
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
public class UtilityPersistenceAdapter implements LoadUtilityPort, SaveUtilityPort {

    private final UtilityJpaRepository repository;
    private final UtilityPersistenceMapper mapper;

    @Inject
    public UtilityPersistenceAdapter(UtilityJpaRepository repository,
                                        UtilityPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Utility> loadById(UtilityId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Utility save(Utility aggregate) {
        UtilityEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
