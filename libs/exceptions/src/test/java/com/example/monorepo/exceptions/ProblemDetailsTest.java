package com.example.monorepo.exceptions;

import com.example.monorepo.exceptions.problem.ProblemDetail;
import com.example.monorepo.exceptions.problem.ProblemDetails;
import com.example.monorepo.exceptions.rest.HttpStatusResolver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProblemDetailsTest {

    @Test
    void notFoundSeTraduceA404YConservaElContexto() {
        NotFoundException exception = new NotFoundException("Authorization", "A-1");
        int status = HttpStatusResolver.toHttpStatus(exception.category());

        ProblemDetail problem = ProblemDetails.from(exception, status, "/api/authorizations/A-1", "trace-1");

        assertEquals(404, problem.status());
        assertEquals("CORE-0002", problem.code());
        assertEquals("trace-1", problem.traceId());
        assertEquals("/api/authorizations/A-1", problem.instance());
        assertNotNull(problem.errors());
    }

    @Test
    void businessSeTraduceA422() {
        assertEquals(422, HttpStatusResolver.toHttpStatus(ErrorCategory.BUSINESS));
    }

    @Test
    void validationSeTraduceA400() {
        ValidationException exception = ValidationException.ofField("amount", "debe ser positivo");
        assertEquals(400, HttpStatusResolver.toHttpStatus(exception.category()));
    }
}
