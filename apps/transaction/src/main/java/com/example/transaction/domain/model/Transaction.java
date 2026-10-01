package com.example.transaction.domain.model;

import com.example.monorepo.core.util.Preconditions;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Agregado de dominio.
 *
 * <p>Java puro: ni anotaciones JPA, ni Jackson, ni CDI. Esta clase se puede
 * instanciar y testear sin levantar absolutamente nada. Su equivalente
 * persistente es {@code infrastructure.persistence.entity.TransactionEntity},
 * y la conversion entre ambos vive en un mapper explicito.</p>
 */
public final class Transaction {

    private final TransactionId id;
    private final String label;
    private final TransactionStatus status;
    private final BigDecimal amount;
    private final Instant createdAt;

    public Transaction(TransactionId id,
                      String label,
                      TransactionStatus status,
                      BigDecimal amount,
                      Instant createdAt) {
        this.id = Preconditions.requireNonNull(id, "id");
        this.label = Preconditions.requireNonBlank(label, "label");
        this.status = Preconditions.requireNonNull(status, "status");
        this.amount = Preconditions.requireNonNull(amount, "amount");
        this.createdAt = Preconditions.requireNonNull(createdAt, "createdAt");
    }

    public TransactionId id() {
        return id;
    }

    public String label() {
        return label;
    }

    public TransactionStatus status() {
        return status;
    }

    public BigDecimal amount() {
        return amount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public boolean isBlocked() {
        return status == TransactionStatus.BLOCKED;
    }
}
