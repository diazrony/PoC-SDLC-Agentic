package com.example.transaction.application.mapper;

import com.example.transaction.application.dto.TransactionResponse;
import com.example.transaction.domain.model.Transaction;

/** Conversion dominio -> DTO de salida. */
public final class TransactionDtoMapper {

    private TransactionDtoMapper() {
    }

    public static TransactionResponse toResponse(Transaction aggregate) {
        return new TransactionResponse(
                aggregate.id().value(),
                aggregate.label(),
                aggregate.status().name(),
                aggregate.amount(),
                aggregate.createdAt());
    }
}
