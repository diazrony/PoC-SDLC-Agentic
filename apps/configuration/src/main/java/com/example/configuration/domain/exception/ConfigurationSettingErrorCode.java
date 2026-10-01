package com.example.configuration.domain.exception;

import com.example.monorepo.exceptions.ErrorCategory;
import com.example.monorepo.exceptions.ErrorCode;

/**
 * Codigos de error propios de esta aplicacion.
 *
 * <p>El catalogo es local: compartir codigos entre microservicios los acopla.
 * Lo que se comparte es el CONTRATO ({@link ErrorCode}), no los valores.</p>
 */
public enum ConfigurationSettingErrorCode implements ErrorCode {

    CONF_NOT_FOUND("CONF-0001", ErrorCategory.NOT_FOUND, "ConfigurationSetting no encontrado"),
    CONF_BLOCKED("CONF-0002", ErrorCategory.BUSINESS, "ConfigurationSetting bloqueado");

    private final String code;
    private final ErrorCategory category;
    private final String title;

    ConfigurationSettingErrorCode(String code, ErrorCategory category, String title) {
        this.code = code;
        this.category = category;
        this.title = title;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public ErrorCategory category() {
        return category;
    }

    @Override
    public String title() {
        return title;
    }
}
