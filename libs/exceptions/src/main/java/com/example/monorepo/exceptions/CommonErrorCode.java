package com.example.monorepo.exceptions;

/** Codigos de error genericos disponibles para todas las aplicaciones. */
public enum CommonErrorCode implements ErrorCode {

    VALIDATION_ERROR("CORE-0001", ErrorCategory.VALIDATION, "Peticion invalida"),
    RESOURCE_NOT_FOUND("CORE-0002", ErrorCategory.NOT_FOUND, "Recurso no encontrado"),
    BUSINESS_RULE_VIOLATION("CORE-0003", ErrorCategory.BUSINESS, "Regla de negocio incumplida"),
    RESOURCE_CONFLICT("CORE-0004", ErrorCategory.CONFLICT, "Conflicto con el estado actual del recurso"),
    INTERNAL_ERROR("CORE-0005", ErrorCategory.TECHNICAL, "Error interno");

    private final String code;
    private final ErrorCategory category;
    private final String title;

    CommonErrorCode(String code, ErrorCategory category, String title) {
        this.code = code;
        this.category = category;
        this.title = title;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorCategory category() {
        return category;
    }

    @Override
    public String title() {
        return title;
    }
}
