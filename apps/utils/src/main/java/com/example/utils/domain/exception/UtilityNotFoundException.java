package com.example.utils.domain.exception;

import com.example.utils.domain.model.UtilityId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class UtilityNotFoundException extends NotFoundException {

    public UtilityNotFoundException(UtilityId id) {
        super(UtilityErrorCode.UTIL_NOT_FOUND, "Utility", id.value());
    }
}
