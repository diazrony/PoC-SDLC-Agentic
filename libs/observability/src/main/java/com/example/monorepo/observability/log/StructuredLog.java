package com.example.monorepo.observability.log;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Constructor de mensajes de log con formato clave=valor.
 *
 * <p>Formato estable y parseable por cualquier agregador (Splunk, Loki, ELK)
 * sin obligar a toda la organizacion a una libreria concreta de logging JSON.</p>
 */
public final class StructuredLog {

    private final Map<String, Object> fields = new LinkedHashMap<>();

    private StructuredLog(String event) {
        fields.put("event", event);
    }

    public static StructuredLog event(String event) {
        return new StructuredLog(event);
    }

    public StructuredLog with(String key, Object value) {
        fields.put(key, value);
        return this;
    }

    public String format() {
        return fields.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return format();
    }
}
