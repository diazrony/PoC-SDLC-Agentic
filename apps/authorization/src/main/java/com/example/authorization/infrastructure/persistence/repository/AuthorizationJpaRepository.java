package com.example.authorization.infrastructure.persistence.repository;

import com.example.authorization.infrastructure.persistence.entity.AuthorizationEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadAuthorizationPort} y {@code SaveAuthorizationPort}.</p>
 */
@ApplicationScoped
public class AuthorizationJpaRepository implements PanacheRepositoryBase<AuthorizationEntity, String> {
}
