package com.example.transaction.infrastructure.persistence.repository;

import com.example.transaction.infrastructure.persistence.entity.TransactionEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadTransactionPort} y {@code SaveTransactionPort}.</p>
 */
@ApplicationScoped
public class TransactionJpaRepository implements PanacheRepositoryBase<TransactionEntity, String> {
}
