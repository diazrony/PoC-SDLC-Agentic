package com.example.retiros.infrastructure.persistence.mapper;

import com.example.retiros.domain.model.Withdrawal;
import com.example.retiros.domain.model.WithdrawalId;
import com.example.retiros.domain.model.WithdrawalStatus;
import com.example.retiros.infrastructure.persistence.entity.WithdrawalEntity;
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
public class WithdrawalPersistenceMapper {

    public Withdrawal toDomain(WithdrawalEntity entity) {
        return new Withdrawal(
                WithdrawalId.of(entity.id),
                entity.label,
                WithdrawalStatus.valueOf(entity.status),
                entity.amount,
                entity.createdAt);
    }

    public WithdrawalEntity toEntity(Withdrawal aggregate) {
        WithdrawalEntity entity = new WithdrawalEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.amount = aggregate.amount();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
