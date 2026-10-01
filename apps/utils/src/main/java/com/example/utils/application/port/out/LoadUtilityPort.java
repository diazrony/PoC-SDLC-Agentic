package com.example.utils.application.port.out;

import com.example.utils.domain.model.Utility;
import com.example.utils.domain.model.UtilityId;

import java.util.Optional;

/**
 * PUERTO DE SALIDA (driven port).
 *
 * <p>Lo DECLARA la capa de aplicacion y lo IMPLEMENTA la infraestructura:
 * asi se invierte la dependencia y la base de datos pasa a ser un detalle.</p>
 */
public interface LoadUtilityPort {

    Optional<Utility> loadById(UtilityId id);
}
