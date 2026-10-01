package com.example.monorepo.http;

import com.example.monorepo.http.context.CorrelationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CorrelationContextTest {

    @Test
    void traceIdUsaElCorrelationIdCuandoExiste() {
        CorrelationContext context = new CorrelationContext();
        context.setCorrelationId("abc-123");
        assertEquals("abc-123", context.traceId());
    }

    @Test
    void traceIdGeneraUnoCuandoNoHayCorrelationId() {
        assertNotNull(new CorrelationContext().traceId());
    }
}
