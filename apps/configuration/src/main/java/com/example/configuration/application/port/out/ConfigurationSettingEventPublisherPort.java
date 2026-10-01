package com.example.configuration.application.port.out;

import com.example.configuration.domain.model.ConfigurationSetting;

/**
 * PUERTO DE SALIDA de publicacion de eventos.
 *
 * <p>Observa que el puerto habla en terminos de DOMINIO. El contrato de
 * integracion compartido (lib-events) aparece unicamente en el adaptador,
 * que es la frontera real del sistema.</p>
 */
public interface ConfigurationSettingEventPublisherPort {

    void publishCreated(ConfigurationSetting aggregate);
}
