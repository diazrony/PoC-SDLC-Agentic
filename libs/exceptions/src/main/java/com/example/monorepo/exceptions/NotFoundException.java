package com.example.monorepo.exceptions;

import java.util.Map;

/** El recurso solicitado no existe. */
public class NotFoundException extends BaseApplicationException {

    public NotFoundException(String resource, String id) {
        super(CommonErrorCode.RESOURCE_NOT_FOUND,
                "No existe %s con identificador %s".formatted(resource, id),
                Map.of("resource", resource, "id", id));
    }

    public NotFoundException(ErrorCode errorCode, String resource, String id) {
        super(errorCode,
                "No existe %s con identificador %s".formatted(resource, id),
                Map.of("resource", resource, "id", id));
    }
}
