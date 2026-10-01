package com.example.monorepo.core.util;

/**
 * Validaciones defensivas minimas para invariantes de dominio.
 *
 * <p>Lanza excepciones estandar de Java a proposito: esta libreria no debe
 * depender de {@code lib-exceptions} para evitar un ciclo conceptual entre
 * el nucleo tecnico y la jerarquia de errores de aplicacion.</p>
 */
public final class Preconditions {

    private Preconditions() {
    }

    public static <T> T requireNonNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " no puede ser null");
        }
        return value;
    }

    public static String requireNonBlank(String value, String name) {
        if (Ids.isBlank(value)) {
            throw new IllegalArgumentException(name + " no puede estar vacio");
        }
        return value;
    }
}
