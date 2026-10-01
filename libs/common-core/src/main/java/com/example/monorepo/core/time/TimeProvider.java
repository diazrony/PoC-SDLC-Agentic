package com.example.monorepo.core.time;

import java.time.Instant;

/**
 * Abstraccion del reloj del sistema.
 *
 * <p>Permite que dominio y capa de aplicacion sean deterministas en test
 * sin acoplarse a {@link Instant#now()}. Es Java puro: ni CDI ni Quarkus.
 * Las aplicaciones la exponen como bean desde {@code infrastructure/config}.</p>
 */
@FunctionalInterface
public interface TimeProvider {

    Instant now();

    /** Implementacion por defecto basada en el reloj del sistema. */
    static TimeProvider system() {
        return Instant::now;
    }

    /** Implementacion fija, util en pruebas. */
    static TimeProvider fixed(Instant instant) {
        return () -> instant;
    }
}
