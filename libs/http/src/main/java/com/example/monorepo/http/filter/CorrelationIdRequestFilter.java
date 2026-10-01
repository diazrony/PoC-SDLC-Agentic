package com.example.monorepo.http.filter;

import com.example.monorepo.core.util.Ids;
import com.example.monorepo.http.constant.ApiHeaders;
import com.example.monorepo.http.constant.HttpConstants;
import com.example.monorepo.http.context.CorrelationContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

/**
 * Toma el correlation id entrante o genera uno nuevo.
 *
 * <p>Se registra automaticamente en cualquier aplicacion que tenga esta libreria
 * en el classpath, gracias al indice Jandex generado durante el build.</p>
 */
@Provider
@Priority(HttpConstants.CORRELATION_FILTER_PRIORITY)
public class CorrelationIdRequestFilter implements ContainerRequestFilter {

    @Inject
    CorrelationContext correlationContext;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String correlationId = requestContext.getHeaderString(ApiHeaders.CORRELATION_ID);
        if (Ids.isBlank(correlationId)) {
            correlationId = Ids.newId();
        }
        correlationContext.setCorrelationId(correlationId);
        correlationContext.setRequestId(Ids.shortId());
    }
}
