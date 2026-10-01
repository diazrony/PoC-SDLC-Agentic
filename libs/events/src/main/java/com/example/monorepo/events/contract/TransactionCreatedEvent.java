package com.example.monorepo.events.contract;

import java.math.BigDecimal;
import java.time.Instant;

/** Se ha registrado una transaccion. Producido por la aplicacion transaction. */
public final class TransactionCreatedEvent extends AbstractDomainEvent {

    public static final String EVENT_TYPE = "transaction.created.v1";

    private final BigDecimal amount;
    private final String currency;

    public TransactionCreatedEvent(String transactionId, BigDecimal amount, String currency, Instant occurredAt) {
        super(transactionId, occurredAt);
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }

    public BigDecimal amount() {
        return amount;
    }

    public String currency() {
        return currency;
    }
}
