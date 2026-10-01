package com.example.monorepo.testing.fixture;

/**
 * Contrato comun de los builders de datos de prueba.
 *
 * <p>Homogeneizar el patron hace que un desarrollador que cambia de equipo
 * reconozca de inmediato como se construyen los datos de test.</p>
 */
@FunctionalInterface
public interface TestDataBuilder<T> {

    T build();
}
