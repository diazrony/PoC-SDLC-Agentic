package com.example.monorepo.events.contract;

import java.math.BigDecimal;
import java.time.Instant;

/** Se ha solicitado un retiro. Producido por la aplicacion retiros. */
public final class WithdrawalRequestedEvent extends AbstractDomainEvent {

    public static final String EVENT_TYPE = "withdrawal.requested.v1";

    private final BigDecimal amount;
    private final String channel;

    public WithdrawalRequestedEvent(String withdrawalId, BigDecimal amount, String channel, Instant occurredAt) {
        super(withdrawalId, occurredAt);
        this.amount = amount;
        this.channel = channel;
    }

    @Override
    public String eventType() {
        return EVENT_TYPE;
    }

    public BigDecimal amount() {
        return amount;
    }

    public String channel() {
        return channel;
    }
}
