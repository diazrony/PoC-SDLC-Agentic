package com.example.monorepo.exceptions;

/**
 * Clasificacion tecnologicamente neutra de un error.
 *
 * <p>Deliberadamente NO contiene codigos HTTP: el dominio no debe conocer HTTP.
 * La traduccion a status se hace en {@code exceptions.rest.HttpStatusResolver}.</p>
 */
public enum ErrorCategory {
    VALIDATION,
    NOT_FOUND,
    BUSINESS,
    CONFLICT,
    TECHNICAL
}
