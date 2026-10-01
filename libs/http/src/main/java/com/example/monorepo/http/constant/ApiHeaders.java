package com.example.monorepo.http.constant;

/** Cabeceras HTTP propias de la organizacion. Una sola definicion para todo el monorepo. */
public final class ApiHeaders {

    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String REQUEST_ID = "X-Request-Id";
    public static final String TRACE_ID = "X-Trace-Id";
    public static final String CLIENT_APPLICATION = "X-Client-Application";
    public static final String API_VERSION = "X-Api-Version";

    private ApiHeaders() {
    }
}
