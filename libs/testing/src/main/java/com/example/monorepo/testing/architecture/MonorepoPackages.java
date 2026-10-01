package com.example.monorepo.testing.architecture;

import java.util.List;

/** Mapa de paquetes del monorepo usado por las reglas de arquitectura. */
public final class MonorepoPackages {

    /** Paquete raiz de las librerias compartidas. */
    public static final String LIBS = "com.example.monorepo";

    /** Paquete raiz de cada aplicacion desplegable. */
    public static final List<String> APPLICATIONS = List.of(
            "com.example.authorization",
            "com.example.transaction",
            "com.example.utils",
            "com.example.configuration",
            "com.example.healthclinic",
            "com.example.retiros");

    private MonorepoPackages() {
    }
}
