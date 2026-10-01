package com.example.retiros.application.port.out;

import com.example.retiros.domain.model.Withdrawal;

/** PUERTO DE SALIDA de escritura. */
public interface SaveWithdrawalPort {

    Withdrawal save(Withdrawal aggregate);
}
