package com.example.healthclinic.infrastructure.adapter.in.rest;

import com.example.healthclinic.application.dto.CreatePatientCommand;
import com.example.healthclinic.application.dto.PatientResponse;
import com.example.healthclinic.application.port.in.CreatePatientUseCase;
import com.example.healthclinic.application.port.in.GetPatientUseCase;
import com.example.healthclinic.domain.model.PatientId;
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
@Path(CoreConstants.API_PREFIX + "/patients")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PatientResource {

    private final GetPatientUseCase getPatientUseCase;
    private final CreatePatientUseCase createPatientUseCase;

    @Inject
    public PatientResource(GetPatientUseCase getPatientUseCase,
                              CreatePatientUseCase createPatientUseCase) {
        this.getPatientUseCase = getPatientUseCase;
        this.createPatientUseCase = createPatientUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getPatient")
    public PatientResponse getById(@PathParam("id") String id) {
        return getPatientUseCase.getById(PatientId.of(id));
    }

    @POST
    @Observed("createPatient")
    public Response create(CreatePatientCommand command) {
        PatientResponse created = createPatientUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/patients/" + created.id(), created);
    }
}
