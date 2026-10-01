package com.example.monorepo.exceptions;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Raiz de todas las excepciones controladas del monorepo.
 *
 * <p>Java puro: sin JAX-RS, sin CDI y sin HTTP. Por eso el dominio de cada
 * aplicacion puede extenderla sin violar la regla hexagonal.</p>
 */
public abstract class BaseApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final transient Map<String, Object> context;

    protected BaseApplicationException(ErrorCode errorCode, String message) {
        this(errorCode, message, Map.of(), null);
    }

    protected BaseApplicationException(ErrorCode errorCode, String message, Map<String, Object> context) {
        this(errorCode, message, context, null);
    }

    protected BaseApplicationException(ErrorCode errorCode,
                                       String message,
                                       Map<String, Object> context,
                                       Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.context = Collections.unmodifiableMap(new LinkedHashMap<>(context));
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public ErrorCategory category() {
        return errorCode.category();
    }

    /** Datos adicionales que el mapper puede publicar de forma segura. */
    public Map<String, Object> context() {
        return context;
    }
}
