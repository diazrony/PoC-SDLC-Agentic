package com.example.monorepo.http.constant;

/** Constantes HTTP internas compartidas. */
public final class HttpConstants {

    /** Content-Type de Problem Details (RFC 7807, actualizado por RFC 9457). */
    public static final String PROBLEM_JSON = "application/problem+json";

    /** Los filtros de correlacion deben ejecutarse de los primeros. */
    public static final int CORRELATION_FILTER_PRIORITY = 1000;

    /** Los filtros de observabilidad se ejecutan justo despues de la correlacion. */
    public static final int OBSERVABILITY_FILTER_PRIORITY = 1100;

    private HttpConstants() {
    }
}
