package com.example.monorepo.http.filter;

import com.example.monorepo.http.constant.ApiHeaders;
import com.example.monorepo.http.constant.HttpConstants;
import com.example.monorepo.http.context.CorrelationContext;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/** Devuelve al cliente los identificadores de correlacion de la peticion. */
@Provider
@Priority(HttpConstants.CORRELATION_FILTER_PRIORITY)
public class CorrelationIdResponseFilter implements ContainerResponseFilter {

    @Inject
    CorrelationContext correlationContext;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().putSingle(ApiHeaders.CORRELATION_ID, correlationContext.getCorrelationId());
        responseContext.getHeaders().putSingle(ApiHeaders.REQUEST_ID, correlationContext.getRequestId());
    }
}
