package com.example.utils.application.service;

import com.example.utils.application.dto.CreateUtilityCommand;
import com.example.utils.application.dto.UtilityResponse;
import com.example.utils.application.mapper.UtilityDtoMapper;
import com.example.utils.application.port.in.CreateUtilityUseCase;
import com.example.utils.application.port.in.GetUtilityUseCase;
import com.example.utils.application.port.out.LoadUtilityPort;
import com.example.utils.application.port.out.SaveUtilityPort;
import com.example.utils.application.port.out.UtilityEventPublisherPort;
import com.example.utils.domain.exception.UtilityNotFoundException;
import com.example.utils.domain.model.Utility;
import com.example.utils.domain.model.UtilityId;
import com.example.utils.domain.model.UtilityStatus;
import com.example.utils.domain.service.UtilityPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.UtilityBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class UtilityService implements GetUtilityUseCase, CreateUtilityUseCase {

    private final LoadUtilityPort loadUtilityPort;
    private final SaveUtilityPort saveUtilityPort;
    private final UtilityEventPublisherPort eventPublisherPort;
    private final UtilityPolicy policy;
    private final TimeProvider timeProvider;

    public UtilityService(LoadUtilityPort loadUtilityPort,
                             SaveUtilityPort saveUtilityPort,
                             UtilityEventPublisherPort eventPublisherPort,
                             UtilityPolicy policy,
                             TimeProvider timeProvider) {
        this.loadUtilityPort = loadUtilityPort;
        this.saveUtilityPort = saveUtilityPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public UtilityResponse getById(UtilityId id) {
        Utility aggregate = loadUtilityPort.loadById(id)
                .orElseThrow(() -> new UtilityNotFoundException(id));
        policy.ensureUsable(aggregate);
        return UtilityDtoMapper.toResponse(aggregate);
    }

    @Override
    public UtilityResponse create(CreateUtilityCommand command) {
        policy.validateForCreation(command.label(), command.threshold());

        Utility aggregate = new Utility(
                UtilityId.of(Ids.newId()),
                command.label(),
                UtilityStatus.ACTIVE,
                command.threshold(),
                timeProvider.now());

        Utility saved = saveUtilityPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return UtilityDtoMapper.toResponse(saved);
    }
}
