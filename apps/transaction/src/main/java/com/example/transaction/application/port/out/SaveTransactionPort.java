package com.example.transaction.application.port.out;

import com.example.transaction.domain.model.Transaction;

/** PUERTO DE SALIDA de escritura. */
public interface SaveTransactionPort {

    Transaction save(Transaction aggregate);
}
