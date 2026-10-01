package com.example.monorepo.exceptions;

import java.util.LinkedHashMap;
import java.util.Map;

/** Datos de entrada invalidos. Transporta los errores por campo. */
public class ValidationException extends BaseApplicationException {

    public ValidationException(String message, Map<String, String> fieldErrors) {
        super(CommonErrorCode.VALIDATION_ERROR, message, toContext(fieldErrors));
    }

    public static ValidationException ofField(String field, String reason) {
        return new ValidationException("Peticion invalida", Map.of(field, reason));
    }

    private static Map<String, Object> toContext(Map<String, String> fieldErrors) {
        Map<String, Object> context = new LinkedHashMap<>();
        fieldErrors.forEach(context::put);
        return context;
    }
}
