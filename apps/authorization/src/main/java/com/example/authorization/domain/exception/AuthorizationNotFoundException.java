package com.example.authorization.domain.exception;

import com.example.authorization.domain.model.AuthorizationId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class AuthorizationNotFoundException extends NotFoundException {

    public AuthorizationNotFoundException(AuthorizationId id) {
        super(AuthorizationErrorCode.AUTH_NOT_FOUND, "Authorization", id.value());
    }
}
