package com.example.utils.infrastructure.persistence.repository;

import com.example.utils.infrastructure.persistence.entity.UtilityEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadUtilityPort} y {@code SaveUtilityPort}.</p>
 */
@ApplicationScoped
public class UtilityJpaRepository implements PanacheRepositoryBase<UtilityEntity, String> {
}
