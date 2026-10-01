package com.example.monorepo.exceptions.problem;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

/**
 * Representacion de error segun RFC 7807 (actualizado por RFC 9457).
 *
 * <p>Los campos {@code type}, {@code title}, {@code status}, {@code detail} e
 * {@code instance} son los del estandar. {@code traceId}, {@code code},
 * {@code timestamp} y {@code errors} son extensiones, algo que el propio RFC
 * permite explicitamente.</p>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetail(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        String traceId,
        String code,
        Instant timestamp,
        Map<String, Object> errors) {
}
