package com.example.utils.application.mapper;

import com.example.utils.application.dto.UtilityResponse;
import com.example.utils.domain.model.Utility;

/** Conversion dominio -> DTO de salida. */
public final class UtilityDtoMapper {

    private UtilityDtoMapper() {
    }

    public static UtilityResponse toResponse(Utility aggregate) {
        return new UtilityResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.threshold(),
                aggregate.createdAt());
    }
}
