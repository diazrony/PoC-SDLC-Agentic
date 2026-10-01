package com.example.healthclinic.application;

import com.example.healthclinic.application.dto.CreatePatientCommand;
import com.example.healthclinic.application.dto.PatientResponse;
import com.example.healthclinic.application.port.out.LoadPatientPort;
import com.example.healthclinic.application.port.out.SavePatientPort;
import com.example.healthclinic.application.port.out.PatientEventPublisherPort;
import com.example.healthclinic.application.service.PatientService;
import com.example.healthclinic.domain.exception.PatientBlockedException;
import com.example.healthclinic.domain.exception.PatientNotFoundException;
import com.example.healthclinic.domain.model.Patient;
import com.example.healthclinic.domain.model.PatientId;
import com.example.healthclinic.domain.model.PatientStatus;
import com.example.healthclinic.domain.service.PatientPolicy;
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
class PatientServiceTest {

    private final Map<String, Patient> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private PatientService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadPatientPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SavePatientPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        PatientEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new PatientService(loadPort, savePort, publisher,
                new PatientPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new Patient(PatientId.of("X-1"), "activo",
                PatientStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        PatientResponse response = service.getById(PatientId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(PatientNotFoundException.class,
                () -> service.getById(PatientId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new Patient(PatientId.of("X-2"), "bloqueado",
                PatientStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(PatientBlockedException.class,
                () -> service.getById(PatientId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        PatientResponse response =
                service.create(new CreatePatientCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreatePatientCommand("nuevo", new BigDecimal("-1"))));
    }
}
