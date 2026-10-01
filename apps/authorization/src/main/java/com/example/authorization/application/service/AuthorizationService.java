package com.example.authorization.application.service;

import com.example.authorization.application.dto.CreateAuthorizationCommand;
import com.example.authorization.application.dto.AuthorizationResponse;
import com.example.authorization.application.mapper.AuthorizationDtoMapper;
import com.example.authorization.application.port.in.CreateAuthorizationUseCase;
import com.example.authorization.application.port.in.GetAuthorizationUseCase;
import com.example.authorization.application.port.out.LoadAuthorizationPort;
import com.example.authorization.application.port.out.SaveAuthorizationPort;
import com.example.authorization.application.port.out.AuthorizationEventPublisherPort;
import com.example.authorization.domain.exception.AuthorizationNotFoundException;
import com.example.authorization.domain.model.Authorization;
import com.example.authorization.domain.model.AuthorizationId;
import com.example.authorization.domain.model.AuthorizationStatus;
import com.example.authorization.domain.service.AuthorizationPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.AuthorizationBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class AuthorizationService implements GetAuthorizationUseCase, CreateAuthorizationUseCase {

    private final LoadAuthorizationPort loadAuthorizationPort;
    private final SaveAuthorizationPort saveAuthorizationPort;
    private final AuthorizationEventPublisherPort eventPublisherPort;
    private final AuthorizationPolicy policy;
    private final TimeProvider timeProvider;

    public AuthorizationService(LoadAuthorizationPort loadAuthorizationPort,
                             SaveAuthorizationPort saveAuthorizationPort,
                             AuthorizationEventPublisherPort eventPublisherPort,
                             AuthorizationPolicy policy,
                             TimeProvider timeProvider) {
        this.loadAuthorizationPort = loadAuthorizationPort;
        this.saveAuthorizationPort = saveAuthorizationPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public AuthorizationResponse getById(AuthorizationId id) {
        Authorization aggregate = loadAuthorizationPort.loadById(id)
                .orElseThrow(() -> new AuthorizationNotFoundException(id));
        policy.ensureUsable(aggregate);
        return AuthorizationDtoMapper.toResponse(aggregate);
    }

    @Override
    public AuthorizationResponse create(CreateAuthorizationCommand command) {
        policy.validateForCreation(command.label(), command.limitAmount());

        Authorization aggregate = new Authorization(
                AuthorizationId.of(Ids.newId()),
                command.label(),
                AuthorizationStatus.ACTIVE,
                command.limitAmount(),
                timeProvider.now());

        Authorization saved = saveAuthorizationPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return AuthorizationDtoMapper.toResponse(saved);
    }
}
