package com.example.transaction.application;

import com.example.transaction.application.dto.CreateTransactionCommand;
import com.example.transaction.application.dto.TransactionResponse;
import com.example.transaction.application.port.out.LoadTransactionPort;
import com.example.transaction.application.port.out.SaveTransactionPort;
import com.example.transaction.application.port.out.TransactionEventPublisherPort;
import com.example.transaction.application.service.TransactionService;
import com.example.transaction.domain.exception.TransactionBlockedException;
import com.example.transaction.domain.exception.TransactionNotFoundException;
import com.example.transaction.domain.model.Transaction;
import com.example.transaction.domain.model.TransactionId;
import com.example.transaction.domain.model.TransactionStatus;
import com.example.transaction.domain.service.TransactionPolicy;
import com.example.monorepo.core.time.TimeProvider;
import com.example.monorepo.exceptions.ValidationException;
import com.example.monorepo.testing.fixture.Fixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test del caso de uso SIN arrancar Quarkus, sin base de datos y sin mocks
 * de libreria: basta con implementar los puertos de salida.
 *
 * <p>Esta es la ventaja practica de la arquitectura hexagonal, y la razon por
 * la que el servicio de aplicacion no lleva anotaciones.</p>
 */
class TransactionServiceTest {

    private final Map<String, Transaction> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private TransactionService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadTransactionPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SaveTransactionPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        TransactionEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new TransactionService(loadPort, savePort, publisher,
                new TransactionPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new Transaction(TransactionId.of("X-1"), "activo",
                TransactionStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        TransactionResponse response = service.getById(TransactionId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(TransactionNotFoundException.class,
                () -> service.getById(TransactionId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new Transaction(TransactionId.of("X-2"), "bloqueado",
                TransactionStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(TransactionBlockedException.class,
                () -> service.getById(TransactionId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        TransactionResponse response =
                service.create(new CreateTransactionCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreateTransactionCommand("nuevo", new BigDecimal("-1"))));
    }
}
