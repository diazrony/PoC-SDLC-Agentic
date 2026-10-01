package com.example.retiros.infrastructure.persistence.repository;

import com.example.retiros.infrastructure.persistence.entity.WithdrawalEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadWithdrawalPort} y {@code SaveWithdrawalPort}.</p>
 */
@ApplicationScoped
public class WithdrawalJpaRepository implements PanacheRepositoryBase<WithdrawalEntity, String> {
}
