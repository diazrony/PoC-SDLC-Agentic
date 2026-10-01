package com.example.utils.infrastructure.persistence.mapper;

import com.example.utils.domain.model.Utility;
import com.example.utils.domain.model.UtilityId;
import com.example.utils.domain.model.UtilityStatus;
import com.example.utils.infrastructure.persistence.entity.UtilityEntity;
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
public class UtilityPersistenceMapper {

    public Utility toDomain(UtilityEntity entity) {
        return new Utility(
                UtilityId.of(entity.id),
                entity.label,
                UtilityStatus.valueOf(entity.status),
                entity.threshold,
                entity.createdAt);
    }

    public UtilityEntity toEntity(Utility aggregate) {
        UtilityEntity entity = new UtilityEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.threshold = aggregate.threshold();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
