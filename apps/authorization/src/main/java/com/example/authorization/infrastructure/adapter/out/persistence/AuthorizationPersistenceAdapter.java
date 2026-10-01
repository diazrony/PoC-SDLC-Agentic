package com.example.authorization.infrastructure.adapter.out.persistence;

import com.example.authorization.application.port.out.LoadAuthorizationPort;
import com.example.authorization.application.port.out.SaveAuthorizationPort;
import com.example.authorization.domain.model.Authorization;
import com.example.authorization.domain.model.AuthorizationId;
import com.example.authorization.infrastructure.persistence.entity.AuthorizationEntity;
import com.example.authorization.infrastructure.persistence.mapper.AuthorizationPersistenceMapper;
import com.example.authorization.infrastructure.persistence.repository.AuthorizationJpaRepository;
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
public class AuthorizationPersistenceAdapter implements LoadAuthorizationPort, SaveAuthorizationPort {

    private final AuthorizationJpaRepository repository;
    private final AuthorizationPersistenceMapper mapper;

    @Inject
    public AuthorizationPersistenceAdapter(AuthorizationJpaRepository repository,
                                        AuthorizationPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Authorization> loadById(AuthorizationId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Authorization save(Authorization aggregate) {
        AuthorizationEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
