package com.example.authorization.infrastructure.persistence.mapper;

import com.example.authorization.domain.model.Authorization;
import com.example.authorization.domain.model.AuthorizationId;
import com.example.authorization.domain.model.AuthorizationStatus;
import com.example.authorization.infrastructure.persistence.entity.AuthorizationEntity;
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
public class AuthorizationPersistenceMapper {

    public Authorization toDomain(AuthorizationEntity entity) {
        return new Authorization(
                AuthorizationId.of(entity.id),
                entity.label,
                AuthorizationStatus.valueOf(entity.status),
                entity.limitAmount,
                entity.createdAt);
    }

    public AuthorizationEntity toEntity(Authorization aggregate) {
        AuthorizationEntity entity = new AuthorizationEntity();
        entity.id = aggregate.id().value();
        entity.label = aggregate.label();
        entity.status = aggregate.status().name();
        entity.limitAmount = aggregate.limitAmount();
        entity.createdAt = aggregate.createdAt();
        return entity;
    }
}
