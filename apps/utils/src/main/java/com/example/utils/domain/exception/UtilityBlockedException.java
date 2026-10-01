package com.example.utils.domain.exception;

import com.example.utils.domain.model.UtilityId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class UtilityBlockedException extends BusinessException {

    public UtilityBlockedException(UtilityId id) {
        super(UtilityErrorCode.UTIL_BLOCKED,
                "El Utility %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
