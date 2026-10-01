package com.example.retiros.domain.exception;

import com.example.retiros.domain.model.WithdrawalId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class WithdrawalNotFoundException extends NotFoundException {

    public WithdrawalNotFoundException(WithdrawalId id) {
        super(WithdrawalErrorCode.RET_NOT_FOUND, "Withdrawal", id.value());
    }
}
