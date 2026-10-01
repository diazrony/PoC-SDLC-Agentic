package com.example.retiros.application.port.in;

import com.example.retiros.application.dto.CreateWithdrawalCommand;
import com.example.retiros.application.dto.WithdrawalResponse;

/** PUERTO DE ENTRADA para el alta del agregado. */
public interface CreateWithdrawalUseCase {

    WithdrawalResponse create(CreateWithdrawalCommand command);
}
