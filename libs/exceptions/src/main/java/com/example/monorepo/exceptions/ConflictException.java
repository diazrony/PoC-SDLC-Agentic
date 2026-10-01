package com.example.monorepo.exceptions;

/** El estado actual del recurso impide completar la operacion. */
public class ConflictException extends BaseApplicationException {

    public ConflictException(String message) {
        super(CommonErrorCode.RESOURCE_CONFLICT, message);
    }

    public ConflictException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
