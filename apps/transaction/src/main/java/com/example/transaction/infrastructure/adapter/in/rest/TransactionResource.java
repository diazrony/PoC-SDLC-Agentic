package com.example.transaction.infrastructure.adapter.in.rest;

import com.example.transaction.application.dto.CreateTransactionCommand;
import com.example.transaction.application.dto.TransactionResponse;
import com.example.transaction.application.port.in.CreateTransactionUseCase;
import com.example.transaction.application.port.in.GetTransactionUseCase;
import com.example.transaction.domain.model.TransactionId;
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
@Path(CoreConstants.API_PREFIX + "/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final GetTransactionUseCase getTransactionUseCase;
    private final CreateTransactionUseCase createTransactionUseCase;

    @Inject
    public TransactionResource(GetTransactionUseCase getTransactionUseCase,
                              CreateTransactionUseCase createTransactionUseCase) {
        this.getTransactionUseCase = getTransactionUseCase;
        this.createTransactionUseCase = createTransactionUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getTransaction")
    public TransactionResponse getById(@PathParam("id") String id) {
        return getTransactionUseCase.getById(TransactionId.of(id));
    }

    @POST
    @Observed("createTransaction")
    public Response create(CreateTransactionCommand command) {
        TransactionResponse created = createTransactionUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/transactions/" + created.id(), created);
    }
}
