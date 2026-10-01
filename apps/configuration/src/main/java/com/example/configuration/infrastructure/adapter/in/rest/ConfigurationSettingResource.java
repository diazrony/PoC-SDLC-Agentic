package com.example.configuration.infrastructure.adapter.in.rest;

import com.example.configuration.application.dto.CreateConfigurationSettingCommand;
import com.example.configuration.application.dto.ConfigurationSettingResponse;
import com.example.configuration.application.port.in.CreateConfigurationSettingUseCase;
import com.example.configuration.application.port.in.GetConfigurationSettingUseCase;
import com.example.configuration.domain.model.ConfigurationSettingId;
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
@Path(CoreConstants.API_PREFIX + "/configurations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfigurationSettingResource {

    private final GetConfigurationSettingUseCase getConfigurationSettingUseCase;
    private final CreateConfigurationSettingUseCase createConfigurationSettingUseCase;

    @Inject
    public ConfigurationSettingResource(GetConfigurationSettingUseCase getConfigurationSettingUseCase,
                              CreateConfigurationSettingUseCase createConfigurationSettingUseCase) {
        this.getConfigurationSettingUseCase = getConfigurationSettingUseCase;
        this.createConfigurationSettingUseCase = createConfigurationSettingUseCase;
    }

    @GET
    @Path("/{id}")
    @Observed("getConfigurationSetting")
    public ConfigurationSettingResponse getById(@PathParam("id") String id) {
        return getConfigurationSettingUseCase.getById(ConfigurationSettingId.of(id));
    }

    @POST
    @Observed("createConfigurationSetting")
    public Response create(CreateConfigurationSettingCommand command) {
        ConfigurationSettingResponse created = createConfigurationSettingUseCase.create(command);
        return ApiResponses.created(
                CoreConstants.API_PREFIX + "/configurations/" + created.id(), created);
    }
}
