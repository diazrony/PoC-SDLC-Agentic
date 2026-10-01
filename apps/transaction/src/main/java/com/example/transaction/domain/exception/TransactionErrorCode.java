package com.example.transaction.domain.exception;

import com.example.monorepo.exceptions.ErrorCategory;
import com.example.monorepo.exceptions.ErrorCode;

/**
 * Codigos de error propios de esta aplicacion.
 *
 * <p>El catalogo es local: compartir codigos entre microservicios los acopla.
 * Lo que se comparte es el CONTRATO ({@link ErrorCode}), no los valores.</p>
 */
public enum TransactionErrorCode implements ErrorCode {

    TRX_NOT_FOUND("TRX-0001", ErrorCategory.NOT_FOUND, "Transaction no encontrado"),
    TRX_BLOCKED("TRX-0002", ErrorCategory.BUSINESS, "Transaction bloqueado");

    private final String code;
    private final ErrorCategory category;
    private final String title;

    TransactionErrorCode(String code, ErrorCategory category, String title) {
        this.code = code;
        this.category = category;
        this.title = title;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorCategory category() {
        return category;
    }

    @Override
    public String title() {
        return title;
    }
}
