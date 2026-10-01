package com.example.retiros.application.port.out;

import com.example.retiros.domain.model.Withdrawal;
import com.example.retiros.domain.model.WithdrawalId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadWithdrawalPort {

    Optional<Withdrawal> loadById(WithdrawalId id);
}
