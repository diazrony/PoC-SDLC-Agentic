package com.example.utils.application.port.in;

import com.example.utils.application.dto.CreateUtilityCommand;
import com.example.utils.application.dto.UtilityResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreateUtilityUseCase {

    UtilityResponse create(CreateUtilityCommand command);
}
