package com.example.transaction.application.service;

import com.example.transaction.application.dto.CreateTransactionCommand;
import com.example.transaction.application.dto.TransactionResponse;
import com.example.transaction.application.mapper.TransactionDtoMapper;
import com.example.transaction.application.port.in.CreateTransactionUseCase;
import com.example.transaction.application.port.in.GetTransactionUseCase;
import com.example.transaction.application.port.out.LoadTransactionPort;
import com.example.transaction.application.port.out.SaveTransactionPort;
import com.example.transaction.application.port.out.TransactionEventPublisherPort;
import com.example.transaction.domain.exception.TransactionNotFoundException;
import com.example.transaction.domain.model.Transaction;
import com.example.transaction.domain.model.TransactionId;
import com.example.transaction.domain.model.TransactionStatus;
import com.example.transaction.domain.service.TransactionPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.core.util.Ids;

/**
 * SERVICIO DE APLICACION: orquesta, no decide.
 *
 * <p>Implementa los puertos de entrada y solo habla con puertos de salida.
 * No lleva ni una anotacion de framework: se expone como bean CDI desde
 * {@code infrastructure.config.TransactionBeanConfiguration}. El precio son
 * cinco lineas de configuracion; el beneficio es que estos casos de uso se
 * testean con un {@code new} y sin arrancar Quarkus.</p>
 */
public class TransactionService implements GetTransactionUseCase, CreateTransactionUseCase {

    private final LoadTransactionPort loadTransactionPort;
    private final SaveTransactionPort saveTransactionPort;
    private final TransactionEventPublisherPort eventPublisherPort;
    private final TransactionPolicy policy;
    private final TimeProvider timeProvider;

    public TransactionService(LoadTransactionPort loadTransactionPort,
                             SaveTransactionPort saveTransactionPort,
                             TransactionEventPublisherPort eventPublisherPort,
                             TransactionPolicy policy,
                             TimeProvider timeProvider) {
        this.loadTransactionPort = loadTransactionPort;
        this.saveTransactionPort = saveTransactionPort;
        this.eventPublisherPort = eventPublisherPort;
        this.policy = policy;
        this.timeProvider = timeProvider;
    }

    @Override
    public TransactionResponse getById(TransactionId id) {
        Transaction aggregate = loadTransactionPort.loadById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));
        policy.ensureUsable(aggregate);
        return TransactionDtoMapper.toResponse(aggregate);
    }

    @Override
    public TransactionResponse create(CreateTransactionCommand command) {
        policy.validateForCreation(command.label(), command.amount());

        Transaction aggregate = new Transaction(
                TransactionId.of(Ids.newId()),
                command.label(),
                TransactionStatus.ACTIVE,
                command.amount(),
                timeProvider.now());

        Transaction saved = saveTransactionPort.save(aggregate);
        eventPublisherPort.publishCreated(saved);
        return TransactionDtoMapper.toResponse(saved);
    }
}
