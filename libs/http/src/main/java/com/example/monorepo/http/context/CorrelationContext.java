package com.example.monorepo.http.context;

import com.example.monorepo.core.util.Ids;
import jakarta.enterprise.context.RequestScoped;

/**
 * Contexto de correlacion de la peticion en curso.
 *
 * <p>Se modela como bean {@code @RequestScoped} y no como {@code ThreadLocal}:
 * en Quarkus una peticion puede saltar entre el event loop y un worker thread,
 * y el contexto de peticion de ArC se propaga correctamente mientras que un
 * ThreadLocal no lo hace.</p>
 */
@RequestScoped
public class CorrelationContext {

    private String correlationId;
    private String requestId;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /** Identificador usado como traceId en los Problem Details y en los logs. */
    public String traceId() {
        return correlationId != null ? correlationId : Ids.newId();
    }
}
