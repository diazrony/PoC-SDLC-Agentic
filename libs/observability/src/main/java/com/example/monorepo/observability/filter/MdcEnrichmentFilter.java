package com.example.monorepo.observability.filter;

import com.example.monorepo.http.constant.ApiHeaders;
import com.example.monorepo.http.constant.HttpConstants;
import com.example.monorepo.http.context.CorrelationContext;
import com.example.monorepo.observability.log.MdcKeys;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.MDC;

/**
 * Publica los identificadores de correlacion en el MDC y los limpia al salir.
 *
 * <p>Se ejecuta despues de {@code CorrelationIdRequestFilter} (prioridad mayor),
 * de forma que el contexto ya esta poblado. Asi cualquier log emitido durante
 * la peticion lleva traceId sin que el codigo de negocio haga nada.</p>
 */
@Provider
@Priority(HttpConstants.OBSERVABILITY_FILTER_PRIORITY)
public class MdcEnrichmentFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Inject
    CorrelationContext correlationContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        MDC.put(MdcKeys.TRACE_ID, correlationContext.traceId());
        MDC.put(MdcKeys.CORRELATION_ID, String.valueOf(correlationContext.getCorrelationId()));
        MDC.put(MdcKeys.REQUEST_ID, String.valueOf(correlationContext.getRequestId()));
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().putSingle(ApiHeaders.TRACE_ID, correlationContext.traceId());
        MDC.remove(MdcKeys.TRACE_ID);
        MDC.remove(MdcKeys.CORRELATION_ID);
        MDC.remove(MdcKeys.REQUEST_ID);
    }
}
