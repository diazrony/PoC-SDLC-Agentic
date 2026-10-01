package com.example.retiros.application;

import com.example.retiros.application.dto.CreateWithdrawalCommand;
import com.example.retiros.application.dto.WithdrawalResponse;
import com.example.retiros.application.port.out.LoadWithdrawalPort;
import com.example.retiros.application.port.out.SaveWithdrawalPort;
import com.example.retiros.application.port.out.WithdrawalEventPublisherPort;
import com.example.retiros.application.service.WithdrawalService;
import com.example.retiros.domain.exception.WithdrawalBlockedException;
import com.example.retiros.domain.exception.WithdrawalNotFoundException;
import com.example.retiros.domain.model.Withdrawal;
import com.example.retiros.domain.model.WithdrawalId;
import com.example.retiros.domain.model.WithdrawalStatus;
import com.example.retiros.domain.service.WithdrawalPolicy;
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
class WithdrawalServiceTest {

    private final Map<String, Withdrawal> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private WithdrawalService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadWithdrawalPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SaveWithdrawalPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        WithdrawalEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new WithdrawalService(loadPort, savePort, publisher,
                new WithdrawalPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new Withdrawal(WithdrawalId.of("X-1"), "activo",
                WithdrawalStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        WithdrawalResponse response = service.getById(WithdrawalId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(WithdrawalNotFoundException.class,
                () -> service.getById(WithdrawalId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new Withdrawal(WithdrawalId.of("X-2"), "bloqueado",
                WithdrawalStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(WithdrawalBlockedException.class,
                () -> service.getById(WithdrawalId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        WithdrawalResponse response =
                service.create(new CreateWithdrawalCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreateWithdrawalCommand("nuevo", new BigDecimal("-1"))));
    }
}
