package com.example.retiros.application.service;

import com.example.retiros.application.dto.CreateWithdrawalCommand;
import com.example.retiros.application.dto.WithdrawalResponse;
import com.example.retiros.application.mapper.WithdrawalDtoMapper;
import com.example.retiros.application.port.in.CreateWithdrawalUseCase;
import com.example.retiros.application.port.in.GetWithdrawalUseCase;
import com.example.retiros.application.port.out.LoadWithdrawalPort;
import com.example.retiros.application.port.out.SaveWithdrawalPort;
import com.example.retiros.application.port.out.WithdrawalEventPublisherPort;
import com.example.retiros.domain.exception.WithdrawalNotFoundException;
import com.example.retiros.domain.model.Withdrawal;
import com.example.retiros.domain.model.WithdrawalId;
import com.example.retiros.domain.model.WithdrawalStatus;
import com.example.retiros.domain.service.WithdrawalPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.WithdrawalBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class WithdrawalService implements GetWithdrawalUseCase, CreateWithdrawalUseCase {

    private final LoadWithdrawalPort loadWithdrawalPort;
    private final SaveWithdrawalPort saveWithdrawalPort;
    private final WithdrawalEventPublisherPort eventPublisherPort;
    private final WithdrawalPolicy policy;
    private final TimeProvider timeProvider;

    public WithdrawalService(LoadWithdrawalPort loadWithdrawalPort,
                             SaveWithdrawalPort saveWithdrawalPort,
                             WithdrawalEventPublisherPort eventPublisherPort,
                             WithdrawalPolicy policy,
                             TimeProvider timeProvider) {
        this.loadWithdrawalPort = loadWithdrawalPort;
        this.saveWithdrawalPort = saveWithdrawalPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public WithdrawalResponse getById(WithdrawalId id) {
        Withdrawal aggregate = loadWithdrawalPort.loadById(id)
                .orElseThrow(() -> new WithdrawalNotFoundException(id));
        policy.ensureUsable(aggregate);
        return WithdrawalDtoMapper.toResponse(aggregate);
    }

    @Override
    public WithdrawalResponse create(CreateWithdrawalCommand command) {
        policy.validateForCreation(command.label(), command.amount());

        Withdrawal aggregate = new Withdrawal(
                WithdrawalId.of(Ids.newId()),
                command.label(),
                WithdrawalStatus.ACTIVE,
                command.amount(),
                timeProvider.now());

        Withdrawal saved = saveWithdrawalPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return WithdrawalDtoMapper.toResponse(saved);
    }
}
