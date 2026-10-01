package com.example.monorepo.exceptions.rest;

import com.example.monorepo.exceptions.ErrorCategory;
import jakarta.ws.rs.core.Response;

/**
 * Unico punto donde una categoria de error se convierte en codigo HTTP.
 *
 * <p>Mantener esta traduccion aqui es lo que permite que el dominio y la capa
 * de aplicacion ignoren por completo el protocolo de transporte.</p>
 */
public final class HttpStatusResolver {

    /** 422: jakarta.ws.rs 3.1 todavia no lo expone como constante en Response.Status. */
    public static final int UNPROCESSABLE_ENTITY = 422;

    private HttpStatusResolver() {
    }

    public static int toHttpStatus(ErrorCategory category) {
        return switch (category) {
            case VALIDATION -> Response.Status.BAD_REQUEST.getStatusCode();
            case NOT_FOUND -> Response.Status.NOT_FOUND.getStatusCode();
            case BUSINESS -> UNPROCESSABLE_ENTITY;
            case CONFLICT -> Response.Status.CONFLICT.getStatusCode();
            case TECHNICAL -> Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        };
    }
}
