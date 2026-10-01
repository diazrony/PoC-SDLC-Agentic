package com.example.utils.application.port.out;

import com.example.utils.domain.model.Utility;

/** PUERTO DE SALIDA de escritura. */
public interface SaveUtilityPort {

    Utility save(Utility aggregate);
}
