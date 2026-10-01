package com.example.authorization.domain.exception;

import com.example.monorepo.exceptions.ErrorCategory;
import com.example.monorepo.exceptions.ErrorCode;

/**
 * Codigos de error propios de esta aplicacion.
 *
 * <p>El catalogo es local: compartir codigos entre microservicios los acopla.
 * Lo que se comparte es el CONTRATO ({@link ErrorCode}), no los valores.</p>
 */
public enum AuthorizationErrorCode implements ErrorCode {

    AUTH_NOT_FOUND("AUTH-0001", ErrorCategory.NOT_FOUND, "Authorization no encontrado"),
    AUTH_BLOCKED("AUTH-0002", ErrorCategory.BUSINESS, "Authorization bloqueado");

    private final String code;
    private final ErrorCategory category;
    private final String title;

    AuthorizationErrorCode(String code, ErrorCategory category, String title) {
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
