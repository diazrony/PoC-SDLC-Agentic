package com.example.monorepo.exceptions;

/** Contrato de un codigo de error. Cada aplicacion define su propio enum. */
public interface ErrorCode {

    /** Codigo estable y publicable, por ejemplo {@code AUTH-0001}. */
    String code();

    /** Categoria tecnica usada para decidir la representacion del error. */
    ErrorCategory category();

    /** Titulo corto y legible, reutilizado como {@code title} del Problem Detail. */
    String title();
}
