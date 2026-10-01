package com.example.retiros.infrastructure.adapter.out.persistence;

import com.example.retiros.application.port.out.LoadWithdrawalPort;
import com.example.retiros.application.port.out.SaveWithdrawalPort;
import com.example.retiros.domain.model.Withdrawal;
import com.example.retiros.domain.model.WithdrawalId;
import com.example.retiros.infrastructure.persistence.entity.WithdrawalEntity;
import com.example.retiros.infrastructure.persistence.mapper.WithdrawalPersistenceMapper;
import com.example.retiros.infrastructure.persistence.repository.WithdrawalJpaRepository;
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
public class WithdrawalPersistenceAdapter implements LoadWithdrawalPort, SaveWithdrawalPort {

    private final WithdrawalJpaRepository repository;
    private final WithdrawalPersistenceMapper mapper;

    @Inject
    public WithdrawalPersistenceAdapter(WithdrawalJpaRepository repository,
                                        WithdrawalPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Withdrawal> loadById(WithdrawalId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Withdrawal save(Withdrawal aggregate) {
        WithdrawalEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
