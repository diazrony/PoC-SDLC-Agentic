package com.example.monorepo.http.response;

import jakarta.ws.rs.core.Response;

/** Azucar sintactico para construir respuestas homogeneas en todas las APIs. */
public final class ApiResponses {

    private ApiResponses() {
    }

    public static <T> Response ok(T body) {
        return Response.ok(body).build();
    }

    public static Response noContent() {
        return Response.noContent().build();
    }

    public static <T> Response created(String location, T body) {
        return Response.status(Response.Status.CREATED)
                .header("Location", location)
                .entity(body)
                .build();
    }
}
