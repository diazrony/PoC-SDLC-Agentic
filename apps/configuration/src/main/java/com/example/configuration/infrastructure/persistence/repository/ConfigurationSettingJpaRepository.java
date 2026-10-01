package com.example.configuration.infrastructure.persistence.repository;

import com.example.configuration.infrastructure.persistence.entity.ConfigurationSettingEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Repositorio tecnico (Panache).
 *
 * <p>Vive en infraestructura a proposito: la capa de aplicacion no lo conoce,
 * solo conoce {@code LoadConfigurationSettingPort} y {@code SaveConfigurationSettingPort}.</p>
 */
@ApplicationScoped
public class ConfigurationSettingJpaRepository implements PanacheRepositoryBase<ConfigurationSettingEntity, String> {
}
