package com.example.transaction.infrastructure.persistence.mapper;

import com.example.transaction.domain.model.Transaction;
import com.example.transaction.domain.model.TransactionId;
import com.example.transaction.domain.model.TransactionStatus;
import com.example.transaction.infrastructure.persistence.entity.TransactionEntity;
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
public class TransactionPersistenceMapper {

    public Transaction toDomain(TransactionEntity entity) {
        return new Transaction(
                TransactionId.of(entity.id),
                entity.label,
                TransactionStatus.valueOf(entity.status),
                entity.amount,
                entity.createdAt);
    }

    public TransactionEntity toEntity(Transaction aggregate) {
        TransactionEntity entity = new TransactionEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.amount = aggregate.amount();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
