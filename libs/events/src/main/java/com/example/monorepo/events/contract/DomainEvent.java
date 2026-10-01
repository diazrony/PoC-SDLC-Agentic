package com.example.monorepo.events.contract;

import java.time.Instant;

/**
 * Contrato minimo de todo evento publicado al exterior.
 *
 * <p>Los metadatos ({@code eventId}, {@code occurredAt}, {@code schemaVersion})
 * son lo verdaderamente reutilizable. El payload es siempre especifico del
 * productor.</p>
 */
public interface DomainEvent {

    String eventId();

    String eventType();

    String aggregateId();

    Instant occurredAt();

    int schemaVersion();
}
