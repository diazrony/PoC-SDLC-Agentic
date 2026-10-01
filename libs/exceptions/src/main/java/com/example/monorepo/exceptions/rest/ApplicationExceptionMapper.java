package com.example.monorepo.exceptions.rest;

import com.example.monorepo.exceptions.BaseApplicationException;
import com.example.monorepo.exceptions.ErrorCategory;
import com.example.monorepo.exceptions.problem.ProblemDetail;
import com.example.monorepo.exceptions.problem.ProblemDetails;
import com.example.monorepo.http.constant.HttpConstants;
import com.example.monorepo.http.context.CorrelationContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * Convierte cualquier {@link BaseApplicationException} en un Problem Detail.
 *
 * <p>Esta clase es la razon de ser de la libreria: las seis aplicaciones
 * obtienen el mismo contrato de error sin escribir una sola linea de mapeo.</p>
 */
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<BaseApplicationException> {

    private static final Logger LOG = Logger.getLogger(ApplicationExceptionMapper.class);

    @Inject
    CorrelationContext correlationContext;

    @Context
    UriInfo uriInfo;

    @Override
    public Response toResponse(BaseApplicationException exception) {
        int status = HttpStatusResolver.toHttpStatus(exception.category());
        String instance = uriInfo != null ? uriInfo.getPath() : "unknown";
        String traceId = correlationContext.traceId();

        if (exception.category() == ErrorCategory.TECHNICAL) {
            LOG.errorf(exception, "[traceId=%s] Error tecnico en %s", traceId, instance);
        } else {
            LOG.debugf("[traceId=%s] %s en %s: %s",
                    traceId, exception.category(), instance, exception.getMessage());
        }

        ProblemDetail problem = ProblemDetails.from(exception, status, instance, traceId);
        return Response.status(status)
                .type(HttpConstants.PROBLEM_JSON)
                .entity(problem)
                .build();
    }
}
