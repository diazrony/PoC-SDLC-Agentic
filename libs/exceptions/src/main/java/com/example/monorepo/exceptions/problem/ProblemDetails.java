package com.example.monorepo.exceptions.problem;

import com.example.monorepo.exceptions.BaseApplicationException;
import com.example.monorepo.exceptions.ErrorCode;

import java.time.Instant;
import java.util.Map;

/** Fabrica de {@link ProblemDetail}. Unico lugar donde se construye el contrato de error. */
public final class ProblemDetails {

    /** Espacio de URIs de la organizacion para documentar tipos de error. */
    public static final String TYPE_BASE_URI = "https://errors.example.com/";

    private ProblemDetails() {
    }

    public static ProblemDetail from(BaseApplicationException exception,
                                     int status,
                                     String instance,
                                     String traceId) {
        ErrorCode errorCode = exception.errorCode();
        return new ProblemDetail(
                TYPE_BASE_URI + errorCode.code().toLowerCase(),
                errorCode.title(),
                status,
                exception.getMessage(),
                instance,
                traceId,
                errorCode.code(),
                Instant.now(),
                exception.context().isEmpty() ? null : exception.context());
    }

    public static ProblemDetail unexpected(String instance, String traceId) {
        return new ProblemDetail(
                TYPE_BASE_URI + "internal-error",
                "Error interno",
                500,
                "Se ha producido un error inesperado. Use el traceId para la investigacion.",
                instance,
                traceId,
                "CORE-0005",
                Instant.now(),
                Map.of());
    }
}
