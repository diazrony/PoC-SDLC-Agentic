package com.example.utils.application.port.in;

import com.example.utils.application.dto.UtilityResponse;
import com.example.utils.domain.model.UtilityId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetUtilityUseCase {

    UtilityResponse getById(UtilityId id);
}
