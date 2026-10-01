package com.example.authorization.application;

import com.example.authorization.application.dto.CreateAuthorizationCommand;
import com.example.authorization.application.dto.AuthorizationResponse;
import com.example.authorization.application.port.out.LoadAuthorizationPort;
import com.example.authorization.application.port.out.SaveAuthorizationPort;
import com.example.authorization.application.port.out.AuthorizationEventPublisherPort;
import com.example.authorization.application.service.AuthorizationService;
import com.example.authorization.domain.exception.AuthorizationBlockedException;
import com.example.authorization.domain.exception.AuthorizationNotFoundException;
import com.example.authorization.domain.model.Authorization;
import com.example.authorization.domain.model.AuthorizationId;
import com.example.authorization.domain.model.AuthorizationStatus;
import com.example.authorization.domain.service.AuthorizationPolicy;
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
class AuthorizationServiceTest {

    private final Map<String, Authorization> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private AuthorizationService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadAuthorizationPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SaveAuthorizationPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        AuthorizationEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new AuthorizationService(loadPort, savePort, publisher,
                new AuthorizationPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new Authorization(AuthorizationId.of("X-1"), "activo",
                AuthorizationStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        AuthorizationResponse response = service.getById(AuthorizationId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(AuthorizationNotFoundException.class,
                () -> service.getById(AuthorizationId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new Authorization(AuthorizationId.of("X-2"), "bloqueado",
                AuthorizationStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(AuthorizationBlockedException.class,
                () -> service.getById(AuthorizationId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        AuthorizationResponse response =
                service.create(new CreateAuthorizationCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreateAuthorizationCommand("nuevo", new BigDecimal("-1"))));
    }
}
