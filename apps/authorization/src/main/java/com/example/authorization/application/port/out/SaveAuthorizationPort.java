package com.example.authorization.application.port.out;

import com.example.authorization.domain.model.Authorization;

/** PUERTO DE SALIDA de escritura. */
public interface SaveAuthorizationPort {

    Authorization save(Authorization aggregate);
}
