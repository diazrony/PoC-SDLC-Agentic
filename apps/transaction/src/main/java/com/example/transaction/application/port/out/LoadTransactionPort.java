package com.example.transaction.application.port.out;

import com.example.transaction.domain.model.Transaction;
import com.example.transaction.domain.model.TransactionId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadTransactionPort {

    Optional<Transaction> loadById(TransactionId id);
}
