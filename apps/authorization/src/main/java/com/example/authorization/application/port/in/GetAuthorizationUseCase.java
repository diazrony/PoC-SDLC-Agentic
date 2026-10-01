package com.example.authorization.application.port.in;

import com.example.authorization.application.dto.AuthorizationResponse;
import com.example.authorization.domain.model.AuthorizationId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetAuthorizationUseCase {

    AuthorizationResponse getById(AuthorizationId id);
}
