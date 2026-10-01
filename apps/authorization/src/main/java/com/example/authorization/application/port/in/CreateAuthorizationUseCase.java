package com.example.authorization.application.port.in;

import com.example.authorization.application.dto.CreateAuthorizationCommand;
import com.example.authorization.application.dto.AuthorizationResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreateAuthorizationUseCase {

    AuthorizationResponse create(CreateAuthorizationCommand command);
}
