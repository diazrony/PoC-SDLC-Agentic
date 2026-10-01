package com.example.configuration.infrastructure.config;

import com.example.monorepo.observability.log.StructuredLog;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;

/** Traza de arranque: deja constancia de con que configuracion se levanta el servicio. */
@ApplicationScoped
public class ConfigurationSettingStartupLogger {

    private static final Logger LOG = Logger.getLogger(ConfigurationSettingStartupLogger.class);

    void onStart(@Observes StartupEvent event, ConfigurationSettingProperties properties) {
        LOG.info(StructuredLog.event("application.started")
                .with("application", "configuration")
                .with("environment", properties.environment())
                .with("maxResults", properties.maxResults())
                .format());
    }
}
