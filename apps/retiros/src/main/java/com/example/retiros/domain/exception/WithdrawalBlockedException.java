package com.example.retiros.domain.exception;

import com.example.retiros.domain.model.WithdrawalId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class WithdrawalBlockedException extends BusinessException {

    public WithdrawalBlockedException(WithdrawalId id) {
        super(WithdrawalErrorCode.RET_BLOCKED,
                "El Withdrawal %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
