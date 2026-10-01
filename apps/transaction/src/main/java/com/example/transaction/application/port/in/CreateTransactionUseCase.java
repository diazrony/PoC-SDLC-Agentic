package com.example.transaction.application.port.in;

import com.example.transaction.application.dto.CreateTransactionCommand;
import com.example.transaction.application.dto.TransactionResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreateTransactionUseCase {

    TransactionResponse create(CreateTransactionCommand command);
}
