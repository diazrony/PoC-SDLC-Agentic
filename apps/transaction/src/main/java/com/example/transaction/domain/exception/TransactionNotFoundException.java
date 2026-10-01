package com.example.transaction.domain.exception;

import com.example.transaction.domain.model.TransactionId;
import com.example.monorepo.exceptions.NotFoundException;

/**
 * Excepcion de dominio.
 *
 * <p>Hereda de la jerarquia compartida de {@code lib-exceptions}, que es Java
 * puro. Gracias a eso el dominio sigue sin conocer HTTP y, aun asi, la
 * aplicacion devuelve automaticamente un Problem Detail 404.</p>
 */
public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(TransactionId id) {
        super(TransactionErrorCode.TRX_NOT_FOUND, "Transaction", id.value());
    }
}
