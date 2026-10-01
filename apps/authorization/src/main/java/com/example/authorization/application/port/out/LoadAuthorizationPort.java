package com.example.authorization.application.port.out;

import com.example.authorization.domain.model.Authorization;
import com.example.authorization.domain.model.AuthorizationId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadAuthorizationPort {

    Optional<Authorization> loadById(AuthorizationId id);
}
