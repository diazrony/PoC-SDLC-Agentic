package com.example.authorization.infrastructure.adapter.in.rest;

import com.example.authorization.application.dto.CreateAuthorizationCommand;
import com.example.authorization.application.dto.AuthorizationResponse;
import com.example.authorization.application.port.in.CreateAuthorizationUseCase;
import com.example.authorization.application.port.in.GetAuthorizationUseCase;
import com.example.authorization.domain.model.AuthorizationId;
import com.example.monorepo.core.constant.CoreConstants;
import com.example.monorepo.http.response.ApiResponses;
import com.example.monorepo.observability.interceptor.Observed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * ADAPTADOR DE ENTRADA (driving adapter).
 *
 * <p>Su unica responsabilidad es traducir HTTP a puertos de entrada. No tiene
 * logica, no captura excepciones (de eso se encargan los ExceptionMapper de
 * lib-exceptions) y no conoce la persistencia.</p>
 */
@Path(CoreConstants.API_PREFIX + "/authorizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorizationResource {

    private final GetAuthorizationUseCase getAuthorizationUseCase;
    private final CreateAuthorizationUseCase createAuthorizationUseCase;

    @Inject
    public AuthorizationResource(GetAuthorizationUseCase getAuthorizationUseCase,
                              CreateAuthorizationUseCase createAuthorizationUseCase) {
        this.getAuthorizationUseCase = getAuthorizationUseCase;
        this.createAuthorizationUseCase = createAuthorizationUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getAuthorization")
    public AuthorizationResponse getById(@PathParam("id") String id) {
        return getAuthorizationUseCase.getById(AuthorizationId.of(id));
    }

    @POST
    @Observed("createAuthorization")
    public Response create(CreateAuthorizationCommand command) {
        AuthorizationResponse created = createAuthorizationUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/authorizations/" + created.id(), created);
    }
}
