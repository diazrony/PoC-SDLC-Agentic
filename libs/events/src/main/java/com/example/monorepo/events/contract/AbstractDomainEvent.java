package com.example.monorepo.events.contract;

import com.example.monorepo.core.util.Ids;
import com.example.monorepo.core.util.Preconditions;

import java.time.Instant;

/** Implementacion base con los metadatos comunes resueltos. */
public abstract class AbstractDomainEvent implements DomainEvent {

    private final String eventId;
    private final String aggregateId;
    private final Instant occurredAt;

    protected AbstractDomainEvent(String aggregateId, Instant occurredAt) {
        this.eventId = Ids.newId();
        this.aggregateId = Preconditions.requireNonBlank(aggregateId, "aggregateId");
        this.occurredAt = Preconditions.requireNonNull(occurredAt, "occurredAt");
    }

    @Override
    public String eventId() {
        return eventId;
    }

    @Override
    public String aggregateId() {
        return aggregateId;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }

    @Override
    public int schemaVersion() {
        return 1;
    }
}
