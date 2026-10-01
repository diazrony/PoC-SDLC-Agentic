package com.example.configuration.infrastructure.adapter.out.persistence;

import com.example.configuration.application.port.out.LoadConfigurationSettingPort;
import com.example.configuration.application.port.out.SaveConfigurationSettingPort;
import com.example.configuration.domain.model.ConfigurationSetting;
import com.example.configuration.domain.model.ConfigurationSettingId;
import com.example.configuration.infrastructure.persistence.entity.ConfigurationSettingEntity;
import com.example.configuration.infrastructure.persistence.mapper.ConfigurationSettingPersistenceMapper;
import com.example.configuration.infrastructure.persistence.repository.ConfigurationSettingJpaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Optional;

/**
 * ADAPTADOR DE SALIDA (driven adapter): implementa los puertos de persistencia.
 *
 * <p>Aqui, y solo aqui, aparecen JPA y las transacciones.</p>
 */
@ApplicationScoped
public class ConfigurationSettingPersistenceAdapter implements LoadConfigurationSettingPort, SaveConfigurationSettingPort {

    private final ConfigurationSettingJpaRepository repository;
    private final ConfigurationSettingPersistenceMapper mapper;

    @Inject
    public ConfigurationSettingPersistenceAdapter(ConfigurationSettingJpaRepository repository,
                                        ConfigurationSettingPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<ConfigurationSetting> loadById(ConfigurationSettingId id) {
        return repository.findByIdOptional(id.value()).map(mapper::toDomain);
    }

    @Override
    @Transactional
    public ConfigurationSetting save(ConfigurationSetting aggregate) {
        ConfigurationSettingEntity entity = mapper.toEntity(aggregate);
        repository.persist(entity);
        return mapper.toDomain(entity);
    }
}
