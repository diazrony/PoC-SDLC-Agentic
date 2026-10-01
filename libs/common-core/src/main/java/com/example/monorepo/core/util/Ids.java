package com.example.monorepo.core.util;

import java.util.UUID;

/** Generacion y validacion de identificadores tecnicos. */
public final class Ids {

    private Ids() {
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public static String shortId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
