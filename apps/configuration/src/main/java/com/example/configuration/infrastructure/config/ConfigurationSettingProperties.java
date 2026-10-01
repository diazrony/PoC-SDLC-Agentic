package com.example.configuration.infrastructure.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Configuracion tipada de la aplicacion.
 *
 * <p>Los valores se resuelven desde {@code application.properties}, que a su
 * vez los toma de variables de entorno. Nunca se versionan secretos.</p>
 */
@ConfigMapping(prefix = "configuration")
public interface ConfigurationSettingProperties {

    /** Entorno logico (local, dev, pre, pro). */
    @WithDefault("local")
    String environment();

    /** Tamanio maximo de pagina admitido por la API. */
    @WithDefault("50")
    int maxResults();
}
