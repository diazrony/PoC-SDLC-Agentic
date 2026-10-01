package com.example.monorepo.events;

import com.example.monorepo.events.contract.TransactionCreatedEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TransactionCreatedEventTest {

    @Test
    void elEventoResuelveSusMetadatosComunes() {
        TransactionCreatedEvent event =
                new TransactionCreatedEvent("T-1", new BigDecimal("10.50"), "EUR", Instant.EPOCH);

        assertNotNull(event.eventId());
        assertEquals("T-1", event.aggregateId());
        assertEquals("transaction.created.v1", event.eventType());
        assertEquals(1, event.schemaVersion());
    }
}
