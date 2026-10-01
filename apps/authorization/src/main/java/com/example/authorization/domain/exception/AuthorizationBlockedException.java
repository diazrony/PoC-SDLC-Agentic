package com.example.authorization.domain.exception;

import com.example.authorization.domain.model.AuthorizationId;
import com.example.monorepo.exceptions.BusinessException;

/** Regla de negocio incumplida: el agregado esta bloqueado. Se traduce a HTTP 422. */
public class AuthorizationBlockedException extends BusinessException {

    public AuthorizationBlockedException(AuthorizationId id) {
        super(AuthorizationErrorCode.AUTH_BLOCKED,
                "El Authorization %s esta bloqueado y no puede consultarse".formatted(id.value()));
    }
}
