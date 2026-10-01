package com.example.monorepo.windows.registry;

/** Fallo tecnico al consultar el Registro. */
public class WindowsRegistryException extends RuntimeException {

    public WindowsRegistryException(String message) {
        super(message);
    }

    public WindowsRegistryException(String message, Throwable cause) {
        super(message, cause);
    }
}
