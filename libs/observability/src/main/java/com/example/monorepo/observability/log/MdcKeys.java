package com.example.monorepo.observability.log;

/** Claves de MDC comunes. Homogeneizarlas es lo que hace agregables los logs. */
public final class MdcKeys {

    public static final String TRACE_ID = "traceId";
    public static final String CORRELATION_ID = "correlationId";
    public static final String REQUEST_ID = "requestId";
    public static final String APPLICATION = "application";
    public static final String OPERATION = "operation";

    private MdcKeys() {
    }
}
