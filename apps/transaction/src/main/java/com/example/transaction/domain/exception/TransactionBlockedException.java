package com.example.transaction.domain.exception;

import com.example.transaction.domain.model.TransactionId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class TransactionBlockedException extends BusinessException {

    public TransactionBlockedException(TransactionId id) {
        super(TransactionErrorCode.TRX_BLOCKED,
                "El Transaction %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
