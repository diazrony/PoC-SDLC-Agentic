package com.example.retiros.infrastructure.adapter.in.rest;

import com.example.retiros.application.dto.CreateWithdrawalCommand;
import com.example.retiros.application.dto.WithdrawalResponse;
import com.example.retiros.application.port.in.CreateWithdrawalUseCase;
import com.example.retiros.application.port.in.GetWithdrawalUseCase;
import com.example.retiros.domain.model.WithdrawalId;
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
@Path(CoreConstants.API_PREFIX + "/retiros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WithdrawalResource {

    private final GetWithdrawalUseCase getWithdrawalUseCase;
    private final CreateWithdrawalUseCase createWithdrawalUseCase;

    @Inject
    public WithdrawalResource(GetWithdrawalUseCase getWithdrawalUseCase,
                              CreateWithdrawalUseCase createWithdrawalUseCase) {
        this.getWithdrawalUseCase = getWithdrawalUseCase;
        this.createWithdrawalUseCase = createWithdrawalUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getWithdrawal")
    public WithdrawalResponse getById(@PathParam("id") String id) {
        return getWithdrawalUseCase.getById(WithdrawalId.of(id));
    }

    @POST
    @Observed("createWithdrawal")
    public Response create(CreateWithdrawalCommand command) {
        WithdrawalResponse created = createWithdrawalUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/retiros/" + created.id(), created);
    }
}
