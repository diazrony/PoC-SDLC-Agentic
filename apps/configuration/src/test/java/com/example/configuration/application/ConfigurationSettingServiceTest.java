package com.example.configuration.application;

import com.example.configuration.application.dto.CreateConfigurationSettingCommand;
import com.example.configuration.application.dto.ConfigurationSettingResponse;
import com.example.configuration.application.port.out.LoadConfigurationSettingPort;
import com.example.configuration.application.port.out.SaveConfigurationSettingPort;
import com.example.configuration.application.port.out.ConfigurationSettingEventPublisherPort;
import com.example.configuration.application.service.ConfigurationSettingService;
import com.example.configuration.domain.exception.ConfigurationSettingBlockedException;
import com.example.configuration.domain.exception.ConfigurationSettingNotFoundException;
import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.configuration.domain.model.ConfigurationSettingStatus;
import com.example.configuration.domain.service.ConfigurationSettingPolicy;
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
class ConfigurationSettingServiceTest {

    private final Map<String, ConfigurationSetting> store = new HashMap<>();
    private final List<String> publishedEvents = new ArrayList<>();

    private ConfigurationSettingService service;

    @BeforeEach
    void setUp() {
        store.clear();
        publishedEvents.clear();

        LoadConfigurationSettingPort loadPort = id -> Optional.ofNullable(store.get(id.value()));
        SaveConfigurationSettingPort savePort = aggregate -> {
            store.put(aggregate.id().value(), aggregate);
            return aggregate;
        };
        ConfigurationSettingEventPublisherPort publisher =
                aggregate -> publishedEvents.add(aggregate.id().value());

        service = new ConfigurationSettingService(loadPort, savePort, publisher,
                new ConfigurationSettingPolicy(), TimeProvider.fixed(Fixtures.FIXED_INSTANT));
    }

    @Test
    void devuelveElAgregadoCuandoExisteYEstaActivo() {
        store.put("X-1", new ConfigurationSetting(ConfigurationSettingId.of("X-1"), "activo",
                ConfigurationSettingStatus.ACTIVE, new BigDecimal("10.00"), Fixtures.FIXED_INSTANT));

        ConfigurationSettingResponse response = service.getById(ConfigurationSettingId.of("X-1"));

        assertEquals("X-1", response.id());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void lanzaNotFoundCuandoNoExiste() {
        assertThrows(ConfigurationSettingNotFoundException.class,
                () -> service.getById(ConfigurationSettingId.of("NO-EXISTE")));
    }

    @Test
    void lanzaErrorDeNegocioCuandoEstaBloqueado() {
        store.put("X-2", new ConfigurationSetting(ConfigurationSettingId.of("X-2"), "bloqueado",
                ConfigurationSettingStatus.BLOCKED, BigDecimal.ZERO, Fixtures.FIXED_INSTANT));

        assertThrows(ConfigurationSettingBlockedException.class,
                () -> service.getById(ConfigurationSettingId.of("X-2")));
    }

    @Test
    void laCreacionPersisteYPublicaUnEvento() {
        ConfigurationSettingResponse response =
                service.create(new CreateConfigurationSettingCommand("nuevo", new BigDecimal("5.00")));

        assertEquals(1, store.size());
        assertEquals(List.of(response.id()), publishedEvents);
        assertEquals(Fixtures.FIXED_INSTANT, response.createdAt());
    }

    @Test
    void laCreacionRechazaValoresNegativos() {
        assertThrows(ValidationException.class,
                () -> service.create(new CreateConfigurationSettingCommand("nuevo", new BigDecimal("-1"))));
    }
}
