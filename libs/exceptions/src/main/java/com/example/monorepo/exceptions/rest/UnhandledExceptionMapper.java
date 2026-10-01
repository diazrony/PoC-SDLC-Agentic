package com.example.monorepo.exceptions.rest;

import com.example.monorepo.exceptions.problem.ProblemDetail;
import com.example.monorepo.exceptions.problem.ProblemDetails;
import com.example.monorepo.http.constant.HttpConstants;
import com.example.monorepo.http.context.CorrelationContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Red de seguridad: ninguna excepcion inesperada debe filtrar stacktraces,
 * nombres de clase ni detalles de infraestructura al cliente.
 */
@Provider
public class UnhandledExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(UnhandledExceptionMapper.class);

    @Inject
    CorrelationContext correlationContext;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable throwable) {
        // Las WebApplicationException ya llevan su propio status (404, 405...).
        if (throwable instanceof WebApplicationException webApplicationException) {
            return webApplicationException.getResponse();
        }

        String instance = uriInfo != null ? uriInfo.getPath() : "unknown";
        String traceId = correlationContext.traceId();
        LOG.errorf(throwable, "[traceId=%s] Excepcion no controlada en %s", traceId, instance);

        ProblemDetail problem = ProblemDetails.unexpected(instance, traceId);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(HttpConstants.PROBLEM_JSON)
                .entity(problem)
                .build();
    }
}
