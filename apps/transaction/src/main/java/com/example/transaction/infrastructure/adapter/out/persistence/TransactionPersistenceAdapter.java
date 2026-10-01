package com.example.transaction.infrastructure.adapter.out.persistence;

import com.example.transaction.application.port.out.LoadTransactionPort;
import com.example.transaction.application.port.out.SaveTransactionPort;
import com.example.transaction.domain.model.Transaction;
import com.example.transaction.domain.model.TransactionId;
import com.example.transaction.infrastructure.persistence.entity.TransactionEntity;
import com.example.transaction.infrastructure.persistence.mapper.TransactionPersistenceMapper;
import com.example.transaction.infrastructure.persistence.repository.TransactionJpaRepository;
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
public class TransactionPersistenceAdapter implements LoadTransactionPort, SaveTransactionPort {

    private final TransactionJpaRepository repository;
    private final TransactionPersistenceMapper mapper;

    @Inject
    public TransactionPersistenceAdapter(TransactionJpaRepository repository,
                                        TransactionPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Transaction> loadById(TransactionId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public Transaction save(Transaction aggregate) {
        TransactionEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
