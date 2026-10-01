package com.example.utils.infrastructure.adapter.in.rest;

import com.example.utils.application.dto.CreateUtilityCommand;
import com.example.utils.application.dto.UtilityResponse;
import com.example.utils.application.port.in.CreateUtilityUseCase;
import com.example.utils.application.port.in.GetUtilityUseCase;
import com.example.utils.domain.model.UtilityId;
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
@Path(CoreConstants.API_PREFIX + "/utils")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UtilityResource {

    private final GetUtilityUseCase getUtilityUseCase;
    private final CreateUtilityUseCase createUtilityUseCase;

    @Inject
    public UtilityResource(GetUtilityUseCase getUtilityUseCase,
                              CreateUtilityUseCase createUtilityUseCase) {
        this.getUtilityUseCase = getUtilityUseCase;
        this.createUtilityUseCase = createUtilityUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getUtility")
    public UtilityResponse getById(@PathParam("id") String id) {
        return getUtilityUseCase.getById(UtilityId.of(id));
    }

    @POST
    @Observed("createUtility")
    public Response create(CreateUtilityCommand command) {
        UtilityResponse created = createUtilityUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/utils/" + created.id(), created);
    }
}
