package com.example.authorization.application.mapper;

import com.example.authorization.application.dto.AuthorizationResponse;
import com.example.authorization.domain.model.Authorization;

/** Conversion dominio -> DTO de salida. */
public final class AuthorizationDtoMapper {

    private AuthorizationDtoMapper() {
    }

    public static AuthorizationResponse toResponse(Authorization aggregate) {
        return new AuthorizationResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.limitAmount(),
                aggregate.createdAt());
    }
}
