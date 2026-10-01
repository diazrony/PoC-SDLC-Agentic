package com.example.utils.application;

import com.example.utils.application.dto.CreateUtilityCommand;
import com.example.utils.application.dto.UtilityResponse;
import com.example.utils.application.port.out.LoadUtilityPort;
import com.example.utils.application.port.out.SaveUtilityPort;
import com.example.utils.application.port.out.UtilityEventPublisherPort;
import com.example.utils.application.service.UtilityService;
import com.example.utils.domain.exception.UtilityBlockedException;
import com.example.utils.domain.exception.UtilityNotFoundException;
import com.example.utils.domain.model.Utility;
import com.example.utils.domain.model.UtilityId;
import com.example.utils.domain.model.UtilityStatus;
import com.example.utils.domain.service.UtilityPolicy;
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
class UtilityServiceTest {

    private final Map<String, Utility> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private UtilityService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadUtilityPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SaveUtilityPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        UtilityEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new UtilityService(loadPort, savePort, publisher,
                new UtilityPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new Utility(UtilityId.of("X-1"), "activo",
                UtilityStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        UtilityResponse response = service.getById(UtilityId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(UtilityNotFoundException.class,
                () -> service.getById(UtilityId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new Utility(UtilityId.of("X-2"), "bloqueado",
                UtilityStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(UtilityBlockedException.class,
                () -> service.getById(UtilityId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        UtilityResponse response =
                service.create(new CreateUtilityCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreateUtilityCommand("nuevo", new BigDecimal("-1"))));
    }
}
