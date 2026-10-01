package com.example.retiros.application.mapper;

import com.example.retiros.application.dto.WithdrawalResponse;
import com.example.retiros.domain.model.Withdrawal;

/** Conversion dominio -> DTO de salida. */
public final class WithdrawalDtoMapper {

    private WithdrawalDtoMapper() {
    }

    public static WithdrawalResponse toResponse(Withdrawal aggregate) {
        return new WithdrawalResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.amount(),
                aggregate.createdAt());
    }
}
