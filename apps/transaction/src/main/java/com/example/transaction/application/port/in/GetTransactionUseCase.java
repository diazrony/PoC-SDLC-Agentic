package com.example.transaction.application.port.in;

import com.example.transaction.application.dto.TransactionResponse;
import com.example.transaction.domain.model.TransactionId;

/**
 * PUERTO DE ENTRADA (driving port).
 *
 * <p>Es lo que el mundo exterior puede pedirle a la aplicacion. El adaptador
 * REST depende de esta interfaz, nunca de la implementacion.</p>
 */
public interface GetTransactionUseCase {

    TransactionResponse getById(TransactionId id);
}
