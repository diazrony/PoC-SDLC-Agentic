package com.example.monorepo.observability;

import com.example.monorepo.observability.log.StructuredLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StructuredLogTest {

    @Test
    void formateaLosCamposComoClaveValor() {
        String line = StructuredLog.event("operation.completed")
                .with("operation", "getAuthorization")
                .with("durationMs", 12)
                .format();

        assertEquals("event=operation.completed operation=getAuthorization durationMs=12", line);
    }
}
