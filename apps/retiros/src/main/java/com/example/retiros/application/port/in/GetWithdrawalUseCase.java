package com.example.retiros.application.port.in;

import com.example.retiros.application.dto.WithdrawalResponse;
import com.example.retiros.domain.model.WithdrawalId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetWithdrawalUseCase {

    WithdrawalResponse getById(WithdrawalId id);
}
